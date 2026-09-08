/* MemoryLink API data layer.
 * Persistent application data lives in MySQL through Spring Boot REST APIs.
 * sessionStorage is used only for temporary UI state (selected lesson, login view, etc.).
 * There is intentionally no localStorage dependency in the application.
 */
(function () {
  const api = {
    memoryLinkLessons: "/api/lessons",
    memoryLinkFlashcards: "/api/flashcards",
    memoryLinkQuizzes: "/api/quizzes",
    memoryLinkStrands: "/api/strands",
    memoryLinkUsers: "/api/students"
  };
  const cache = sessionStorage;
  const pending = new Map();

  const originalAddEventListener = document.addEventListener.bind(document);
  const domReadyCallbacks = [];
  let domReady = false;
  let hydrationComplete = false;

  document.addEventListener = function(type, listener, options) {
    if (type === "DOMContentLoaded" && typeof listener === "function" && !domReady) {
      domReadyCallbacks.push({ listener, options });
      return;
    }
    return originalAddEventListener(type, listener, options);
  };

  function rawGet(key) { return cache.getItem(key); }
  function rawSet(key, value) { cache.setItem(key, value); }
  function rawRemove(key) { cache.removeItem(key); }

  async function sync(key, value) {
    const endpoint = api[key];
    if (!endpoint || pending.has(key)) return;
    let data;
    try { data = JSON.parse(value); } catch { return; }
    if (!Array.isArray(data)) return;

    const promise = fetch(endpoint + "/sync", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    }).then(async response => {
      if (!response.ok) throw new Error("API sync failed: " + response.status);
      const saved = await response.json();
      rawSet(key, JSON.stringify(saved));
      return saved;
    }).catch(error => {
      console.error("MemoryLink API sync failed for " + key, error);
      throw error;
    }).finally(() => pending.delete(key));

    pending.set(key, promise);
    return promise;
  }

  async function hydrate(key) {
    const endpoint = api[key];
    if (!endpoint) return;
    try {
      const response = await fetch(endpoint, { headers: { "Accept": "application/json" } });
      if (!response.ok) return;
      const dbData = await response.json();
      if (Array.isArray(dbData) && dbData.length > 0) {
        rawSet(key, JSON.stringify(dbData));
      }
    } catch (error) {
      console.warn("MemoryLink API load failed for " + key, error);
    }
  }

  window.memoryStore = {
    getItem: rawGet,
    setItem(key, value) {
      rawSet(key, value);
      if (api[key]) sync(key, value).catch(() => {});
    },
    removeItem: rawRemove,
    clear() { cache.clear(); },
    key(index) { return cache.key(index); },
    get length() { return cache.length; },
    async refresh(key) { await hydrate(key); return rawGet(key); },
    async sync(key) { return sync(key, rawGet(key)); },
    api
  };

  // Keep the old pages' behavior, but make their storage calls API-backed.
  // This is a compatibility layer only; persistent records are stored by the REST API.
  window.memoryLinkReady = Promise.all(Object.keys(api).map(hydrate)).then(() => {
    hydrationComplete = true;
    if (domReady) releaseDomReady();
  });

  function releaseDomReady() {
    if (!hydrationComplete || !domReady) return;
    const callbacks = domReadyCallbacks.splice(0);
    callbacks.forEach(({ listener }) => {
      try { listener.call(document, new Event("DOMContentLoaded")); }
      catch (error) { setTimeout(() => { throw error; }, 0); }
    });
  }

  originalAddEventListener("DOMContentLoaded", function () {
    domReady = true;
    releaseDomReady();
  });
})();
