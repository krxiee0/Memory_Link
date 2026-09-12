/* MemoryLink API data layer.
 *
 * Persistent application data lives in MySQL through Spring Boot REST APIs.
 * sessionStorage is used only as a temporary browser cache/UI state.
 *
 * IMPORTANT:
 * - New records created by the frontend may have temporary IDs.
 * - Temporary IDs are removed before sending NEW records to the backend.
 * - Existing database IDs are preserved.
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


    /*
     * ---------------------------------------------------------
     * DOM READY CONTROL
     * ---------------------------------------------------------
     */

    document.addEventListener = function (type, listener, options) {

        if (
            type === "DOMContentLoaded" &&
            typeof listener === "function" &&
            !domReady
        ) {
            domReadyCallbacks.push({
                listener,
                options
            });

            return;
        }

        return originalAddEventListener(
            type,
            listener,
            options
        );
    };


    /*
     * ---------------------------------------------------------
     * SESSION STORAGE
     * ---------------------------------------------------------
     */

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
     * ---------------------------------------------------------
     * SYNC QUEUES
     *
     * Prevents multiple saves from racing each other.
     * ---------------------------------------------------------
     */

    const syncQueues = new Map();


    /*
     * ---------------------------------------------------------
     * PREPARE DATA BEFORE DATABASE SYNC
     * ---------------------------------------------------------
     *
     * admin_lessons.html currently creates new lessons like:
     *
     * id: Date.now()
     *
     * Example:
     * 1750000000000
     *
     * That is NOT a database ID.
     *
     * Existing database IDs are normally small values such as:
     * 1, 2, 3, 4...
     *
     * Therefore:
     * - existing normal IDs are preserved
     * - Date.now() temporary IDs are changed to null
     *
     * Spring Boot/JPA can then generate the real AUTO_INCREMENT ID.
     */

    function prepareDataForSync(key, data) {

        if (!Array.isArray(data)) {
            return data;
        }

        return data.map(item => {

            if (!item || typeof item !== "object") {
                return item;
            }

            const copy = {
                ...item
            };


            /*
             * LESSONS
             *
             * Remove frontend-generated Date.now() IDs.
             */
            if (key === "memoryLinkLessons") {

                if (
                    typeof copy.id === "number" &&
                    copy.id > 100000000000
                ) {
                    copy.id = null;
                }

                /*
                 * Sometimes an ID may arrive as a string.
                 */
                if (
                    typeof copy.id === "string" &&
                    /^\d+$/.test(copy.id) &&
                    Number(copy.id) > 100000000000
                ) {
                    copy.id = null;
                }
            }

            return copy;
        });
    }


    /*
     * ---------------------------------------------------------
     * SYNC TO SPRING BOOT / MYSQL
     * ---------------------------------------------------------
     */

    async function sync(key, value) {

        const endpoint = api[key];

        if (!endpoint) {
            return;
        }

        let data;

        try {

            data = JSON.parse(value);

        } catch (error) {

            console.error(
                "MemoryLink: Invalid JSON for",
                key,
                error
            );

            return;
        }


        if (!Array.isArray(data)) {

            console.warn(
                "MemoryLink: Expected array for",
                key
            );

            return;
        }


        /*
         * Prepare records before sending them.
         */
        const preparedData =
            prepareDataForSync(key, data);


        /*
         * If another sync is already running,
         * wait for it before starting this one.
         */
        const previous =
            syncQueues.get(key) ||
            Promise.resolve();


        const current = previous
            .catch(() => {})
            .then(async () => {

                console.log(
                    "MemoryLink: syncing",
                    key,
                    preparedData
                );


                const response = await fetch(
                    endpoint + "/sync",
                    {
                        method: "PUT",

                        headers: {
                            "Content-Type":
                                "application/json",

                            "Accept":
                                "application/json"
                        },

                        body:
                            JSON.stringify(preparedData)
                    }
                );


                /*
                 * Backend rejected the request.
                 */
                if (!response.ok) {

                    const text =
                        await response.text();

                    throw new Error(
                        "API sync failed: " +
                        response.status +
                        " " +
                        text
                    );
                }


                /*
                 * Backend should return
                 * the saved database records.
                 */
                const saved =
                    await response.json();


                if (Array.isArray(saved)) {

                    /*
                     * IMPORTANT:
                     *
                     * Replace temporary frontend IDs
                     * with the real database IDs returned
                     * by Spring Boot.
                     */
                    rawSet(
                        key,
                        JSON.stringify(saved)
                    );
                }


                console.log(
                    "MemoryLink API sync successful:",
                    key,
                    saved
                );


                return saved;
            });


        syncQueues.set(
            key,
            current
        );


        try {

            return await current;

        } finally {

            if (
                syncQueues.get(key) === current
            ) {
                syncQueues.delete(key);
            }
        }
    }


    /*
     * ---------------------------------------------------------
     * LOAD DATA FROM MYSQL
     * ---------------------------------------------------------
     */

    async function hydrate(key) {

        const endpoint = api[key];

        if (!endpoint) {
            return;
        }


        try {

            const response =
                await fetch(
                    endpoint,
                    {
                        headers: {
                            "Accept":
                                "application/json"
                        }
                    }
                );


            if (!response.ok) {

                console.warn(
                    "MemoryLink API load failed for " +
                    key +
                    ": " +
                    response.status
                );

                return;
            }


            const dbData =
                await response.json();


            if (Array.isArray(dbData)) {

                /*
                 * Database is the source of truth.
                 *
                 * Even if database is empty,
                 * replace cache with [].
                 */
                rawSet(
                    key,
                    JSON.stringify(dbData)
                );


                console.log(
                    "MemoryLink loaded from database:",
                    key,
                    dbData
                );
            }

        } catch (error) {

            console.warn(
                "MemoryLink API load failed for " +
                key,
                error
            );
        }
    }


    /*
     * ---------------------------------------------------------
     * MEMORY STORE
     * ---------------------------------------------------------
     */

    window.memoryStore = {

        getItem(key) {

            return rawGet(key);
        },


        setItem(key, value) {

            /*
             * Always update browser cache immediately.
             */
            rawSet(
                key,
                value
            );


            /*
             * If this key is connected to a backend API,
             * immediately synchronize it with MySQL.
             */
            if (api[key]) {

                sync(
                    key,
                    value
                ).catch(error => {

                    console.error(
                        "MemoryLink API sync failed for " +
                        key,
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


        /*
         * Manually reload one data type
         * from MySQL.
         */
        async refresh(key) {

            await hydrate(key);

            return rawGet(key);
        },


        /*
         * Manually force synchronization.
         */
        async sync(key) {

            const value =
                rawGet(key);

            if (!value) {
                return;
            }

            return sync(
                key,
                value
            );
        },


        /*
         * Expose API endpoints for debugging.
         */
        api
    };


    /*
     * ---------------------------------------------------------
     * INITIAL DATABASE HYDRATION
     * ---------------------------------------------------------
     *
     * Load:
     * - Lessons
     * - Flashcards
     * - Quizzes
     * - Strands
     * - Students
     *
     * before releasing DOMContentLoaded.
     */

    window.memoryLinkReady =
        Promise.all(
            Object.keys(api).map(hydrate)
        )
        .then(() => {

            hydrationComplete = true;


            if (domReady) {

                releaseDomReady();
            }

        })
        .catch(error => {

            console.error(
                "MemoryLink database initialization failed:",
                error
            );

            /*
             * Don't permanently block the page
             * if one API fails.
             */
            hydrationComplete = true;

            if (domReady) {
                releaseDomReady();
            }
        });


    /*
     * ---------------------------------------------------------
     * RELEASE DOM READY
     * ---------------------------------------------------------
     */

    function releaseDomReady() {

        if (
            !hydrationComplete ||
            !domReady
        ) {
            return;
        }


        const callbacks =
            domReadyCallbacks.splice(0);


        callbacks.forEach(
            ({ listener }) => {

                try {

                    listener.call(
                        document,
                        new Event(
                            "DOMContentLoaded"
                        )
                    );

                } catch (error) {

                    setTimeout(() => {
                        throw error;
                    }, 0);
                }

            }
        );
    }


    /*
     * ---------------------------------------------------------
     * ORIGINAL DOM CONTENT LOADED
     * ---------------------------------------------------------
     */

    originalAddEventListener(
        "DOMContentLoaded",
        function () {

            domReady = true;

            releaseDomReady();

        }
    );

})();
