/* MemoryLink API data layer.
 * Persistent application data lives in MySQL through Spring Boot REST APIs.
 * sessionStorage is used only for temporary UI state.
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

  const originalAddEventListener =
    document.addEventListener.bind(document);

  const domReadyCallbacks = [];
  let domReady = false;
  let hydrationComplete = false;

  document.addEventListener = function (type, listener, options) {
    if (
      type === "DOMContentLoaded" &&
      typeof listener === "function" &&
      !domReady
    ) {
      domReadyCallbacks.push({ listener, options });
      return;
    }

    return originalAddEventListener(type, listener, options);
  };

  function rawGet(key) {
    return cache.getItem(key);
  }

  function rawSet(key, value) {
    cache.setItem(key, value);
  }

  function rawRemove(key) {
    cache.removeItem(key);
  }

  /*
   * Always send the latest data.
   * If another sync is running, wait for it first.
   */
  const syncQueues = new Map();

  async function sync(key, value) {
    const endpoint = api[key];

    if (!endpoint) return;

    let data;

    try {
      data = JSON.parse(value);
    } catch {
      return;
    }

    if (!Array.isArray(data)) return;

    const previous = syncQueues.get(key) || Promise.resolve();

    const current = previous
      .catch(() => {})
      .then(async () => {
        const response = await fetch(endpoint + "/sync", {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
          },
          body: JSON.stringify(data)
        });

        if (!response.ok) {
          const text = await response.text();
          throw new Error(
            "API sync failed: " +
            response.status +
            " " +
            text
          );
        }

        const saved = await response.json();

        if (Array.isArray(saved)) {
          rawSet(key, JSON.stringify(saved));
        }

        console.log(
          "MemoryLink API sync successful:",
          key,
          saved
        );

        return saved;
      });

    syncQueues.set(key, current);

    try {
      return await current;
    } finally {
      if (syncQueues.get(key) === current) {
        syncQueues.delete(key);
      }
    }
  }

  async function hydrate(key) {
    const endpoint = api[key];

    if (!endpoint) return;

    try {
      const response = await fetch(endpoint, {
        headers: {
          "Accept": "application/json"
        }
      });

      if (!response.ok) {
        console.warn(
          "MemoryLink API load failed for " +
          key +
          ": " +
          response.status
        );
        return;
      }

      const dbData = await response.json();

      if (Array.isArray(dbData)) {
        /*
         * Important:
         * Even if database is empty, replace the cache with [].
         */
        rawSet(key, JSON.stringify(dbData));

        console.log(
          "MemoryLink loaded from database:",
          key,
          dbData
        );
      }
    } catch (error) {
      console.warn(
        "MemoryLink API load failed for " + key,
        error
      );
    }
  }

  window.memoryStore = {
    getItem(key) {
      return rawGet(key);
    },

    setItem(key, value) {
      rawSet(key, value);

      if (api[key]) {
        /*
         * Don't silently lose sync errors.
         */
        sync(key, value).catch(error => {
          console.error(
            "MemoryLink API sync failed for " + key,
            error
          );
        });
      }
    },

    removeItem(key) {
      rawRemove(key);
    },

    clear() {
      cache.clear();
    },

    key(index) {
      return cache.key(index);
    },

    get length() {
      return cache.length;
    },

    async refresh(key) {
      await hydrate(key);
      return rawGet(key);
    },

    async sync(key) {
      return sync(key, rawGet(key));
    },

    api
  };

  window.memoryLinkReady =
    Promise.all(Object.keys(api).map(hydrate))
      .then(() => {
        hydrationComplete = true;

        if (domReady) {
          releaseDomReady();
        }
      });

  function releaseDomReady() {
    if (!hydrationComplete || !domReady) return;

    const callbacks = domReadyCallbacks.splice(0);

    callbacks.forEach(({ listener }) => {
      try {
        listener.call(
          document,
          new Event("DOMContentLoaded")
        );
      } catch (error) {
        setTimeout(() => {
          throw error;
        }, 0);
      }
    });
  }

  originalAddEventListener(
    "DOMContentLoaded",
    function () {
      domReady = true;
      releaseDomReady();
    }
  );
})();
