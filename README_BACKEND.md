# MemoryLink Backend

Learning Strands, Flashcards, and Quizzes are persisted through Spring Boot REST APIs and MySQL.

Endpoints:
- GET/POST `/api/strands`
- GET/PUT/DELETE `/api/strands/{id}`
- PUT `/api/strands/sync`
- GET/POST `/api/flashcards`
- GET/PUT/DELETE `/api/flashcards/{id}`
- PUT `/api/flashcards/sync`
- GET/POST `/api/quizzes`
- GET/PUT/DELETE `/api/quizzes/{id}`
- PUT `/api/quizzes/sync`

Run from `MemoryLink02/ml`:
`mvn clean spring-boot:run`

The frontend's `db-sync.js` hydrates these API collections and its `memoryStore.setItem()` syncs persistent collections to the backend.
