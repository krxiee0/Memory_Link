package com.memorylink.controller;

import com.memorylink.model.Flashcard;
import com.memorylink.repository.FlashcardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    private final FlashcardRepository repo;

    public FlashcardController(FlashcardRepository repo) {
        this.repo = repo;
    }

    // =========================================================
    // GET ALL FLASHCARDS
    // =========================================================
    @GetMapping
    public List<Flashcard> all() {
        return repo.findAll();
    }


    // =========================================================
    // GET ONE FLASHCARD
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<Flashcard> one(@PathVariable Long id) {

        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // =========================================================
    // CREATE FLASHCARD
    // =========================================================
    @PostMapping
    public ResponseEntity<Flashcard> create(
            @RequestBody Flashcard card) {

        // Let MySQL/JPA generate the real ID
        card.setId(null);

        return ResponseEntity.ok(
                repo.save(card)
        );
    }


    // =========================================================
    // UPDATE FLASHCARD
    // =========================================================
    @PutMapping("/{id}")
    public ResponseEntity<Flashcard> update(
            @PathVariable Long id,
            @RequestBody Flashcard incoming) {

        return repo.findById(id)
                .map(existing -> {

                    if (incoming.getStrand() != null) {
                        existing.setStrand(
                                incoming.getStrand()
                        );
                    }

                    if (incoming.getDifficulty() != null) {
                        existing.setDifficulty(
                                incoming.getDifficulty()
                        );
                    }

                    if (incoming.getQuestion() != null) {
                        existing.setQuestion(
                                incoming.getQuestion()
                        );
                    }

                    if (incoming.getAnswer() != null) {
                        existing.setAnswer(
                                incoming.getAnswer()
                        );
                    }

                    if (incoming.getLesson() != null) {
                        existing.setLesson(
                                incoming.getLesson()
                        );
                    }

                    if (incoming.getLessonNumber() != null) {
                        existing.setLessonNumber(
                                incoming.getLessonNumber()
                        );
                    }

                    return ResponseEntity.ok(
                            repo.save(existing)
                    );
                })
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }


    // =========================================================
    // DELETE ONE FLASHCARD
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // SYNC FRONTEND DATA WITH MYSQL
    // =========================================================
    @PutMapping("/sync")
    public List<Flashcard> sync(
            @RequestBody List<Flashcard> data) {

        /*
         * IDs that currently exist in the frontend.
         *
         * These records should remain in MySQL.
         */
        Set<Long> incomingIds = new HashSet<>();


        // =====================================================
        // UPDATE EXISTING / CREATE NEW
        // =====================================================

        for (Flashcard incoming : data) {

            // Ignore invalid flashcards
            if (incoming == null) {
                continue;
            }

            if (incoming.getQuestion() == null ||
                    incoming.getQuestion().isBlank()) {
                continue;
            }


            // -------------------------------------------------
            // EXISTING DATABASE RECORD
            // -------------------------------------------------
            if (incoming.getId() != null &&
                    repo.existsById(incoming.getId())) {

                Flashcard existing =
                        repo.findById(incoming.getId())
                                .orElse(null);

                if (existing != null) {

                    existing.setStrand(
                            incoming.getStrand()
                    );

                    existing.setDifficulty(
                            incoming.getDifficulty()
                    );

                    existing.setQuestion(
                            incoming.getQuestion()
                    );

                    existing.setAnswer(
                            incoming.getAnswer()
                    );

                    existing.setLesson(
                            incoming.getLesson()
                    );

                    existing.setLessonNumber(
                            incoming.getLessonNumber()
                    );

                    repo.save(existing);

                    incomingIds.add(
                            existing.getId()
                    );
                }
            }


            // -------------------------------------------------
            // NEW FLASHCARD
            // -------------------------------------------------
            else {

                /*
                 * Important:
                 * frontend temporary ID must not be saved.
                 *
                 * MySQL AUTO_INCREMENT generates the real ID.
                 */
                incoming.setId(null);

                Flashcard saved =
                        repo.save(incoming);

                if (saved.getId() != null) {
                    incomingIds.add(
                            saved.getId()
                    );
                }
            }
        }


        // =====================================================
        // DELETE RECORDS REMOVED FROM FRONTEND
        // =====================================================

        List<Flashcard> databaseCards =
                repo.findAll();

        for (Flashcard databaseCard : databaseCards) {

            Long databaseId =
                    databaseCard.getId();

            /*
             * If the database record is NOT included
             * in the frontend's current list,
             * it means the user deleted it.
             */
            if (databaseId != null &&
                    !incomingIds.contains(databaseId)) {

                repo.deleteById(databaseId);
            }
        }


        // =====================================================
        // RETURN THE ACTUAL DATABASE DATA
        // =====================================================

        return repo.findAll();
    }
}
