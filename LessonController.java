package com.memorylink.controller;

import com.memorylink.model.Lesson;
import com.memorylink.repository.LessonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonRepository repo;

    public LessonController(LessonRepository repo) {
        this.repo = repo;
    }

    // GET ALL LESSONS
    @GetMapping
    public List<Lesson> all() {
        return repo.findAll();
    }


    // GET ONE LESSON
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> one(@PathVariable Long id) {

        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // CREATE NEW LESSON
    @PostMapping
    public ResponseEntity<Lesson> create(
            @RequestBody Lesson lesson) {

        // Let MySQL generate the ID
        lesson.setId(null);

        normalize(lesson);

        Lesson saved = repo.save(lesson);

        return ResponseEntity.ok(saved);
    }


    // UPDATE EXISTING LESSON
    @PutMapping("/{id}")
    public ResponseEntity<Lesson> update(
            @PathVariable Long id,
            @RequestBody Lesson incoming) {

        return repo.findById(id)
                .map(existing -> {

                    existing.setTitle(incoming.getTitle());
                    existing.setCategory(incoming.getCategory());
                    existing.setDuration(incoming.getDuration());
                    existing.setDescription(incoming.getDescription());
                    existing.setNumberOfLessons(
                            incoming.getNumberOfLessons()
                    );
                    existing.setStatus(incoming.getStatus());
                    existing.setSubmittedBy(
                            incoming.getSubmittedBy()
                    );
                    existing.setSubLessons(
                            incoming.getSubLessons()
                    );

                    normalize(existing);

                    return ResponseEntity.ok(
                            repo.save(existing)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // DELETE LESSON
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }


    /*
     * SYNC LESSONS FROM FRONTEND
     *
     * Existing database records are updated.
     * New records are inserted.
     *
     * We DO NOT delete the whole table.
     */
    @PutMapping("/sync")
    public List<Lesson> sync(
            @RequestBody List<Lesson> data) {

        if (data == null) {
            return repo.findAll();
        }

        List<Lesson> result = new ArrayList<>();

        for (Lesson incoming : data) {

            if (incoming == null) {
                continue;
            }


            /*
             * NEW LESSON
             *
             * id == null means MySQL/JPA
             * should generate the ID.
             */
            if (incoming.getId() == null) {

                normalize(incoming);

                Lesson saved =
                        repo.save(incoming);

                result.add(saved);

                continue;
            }


            /*
             * EXISTING LESSON
             *
             * Find it first.
             */
            Lesson existing =
                    repo.findById(incoming.getId())
                            .orElse(null);


            if (existing != null) {

                existing.setTitle(
                        incoming.getTitle()
                );

                existing.setCategory(
                        incoming.getCategory()
                );

                existing.setDuration(
                        incoming.getDuration()
                );

                existing.setDescription(
                        incoming.getDescription()
                );

                existing.setNumberOfLessons(
                        incoming.getNumberOfLessons()
                );

                existing.setStatus(
                        incoming.getStatus()
                );

                existing.setSubmittedBy(
                        incoming.getSubmittedBy()
                );

                existing.setSubLessons(
                        incoming.getSubLessons()
                );

                normalize(existing);

                Lesson saved =
                        repo.save(existing);

                result.add(saved);

            } else {

                /*
                 * The frontend has an ID that does not
                 * exist in MySQL.
                 *
                 * Treat it as a NEW record.
                 */
                incoming.setId(null);

                normalize(incoming);

                Lesson saved =
                        repo.save(incoming);

                result.add(saved);
            }
        }

        return repo.findAll();
    }


    /*
     * DEFAULT VALUES
     */
    private void normalize(Lesson lesson) {

        if (lesson.getStatus() == null ||
                lesson.getStatus().isBlank()) {

            lesson.setStatus("approved");
        }

        if (lesson.getSubLessons() == null) {

            lesson.setSubLessons(
                    new ArrayList<>()
            );
        }
    }
}
