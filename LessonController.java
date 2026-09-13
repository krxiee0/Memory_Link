package com.memorylink.controller;

import com.memorylink.model.Lesson;
import com.memorylink.repository.LessonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*")
public class LessonController {

    private final LessonRepository repo;

    public LessonController(LessonRepository repo) {
        this.repo = repo;
    }

    // ═══════════════════════════════════════════════════════════
    // GET ALL LESSONS
    // ═══════════════════════════════════════════════════════════
    @GetMapping
    public List<Lesson> all() {
        return repo.findAll();
    }

    // ═══════════════════════════════════════════════════════════
    // GET ONE LESSON
    // ═══════════════════════════════════════════════════════════
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> one(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ═══════════════════════════════════════════════════════════
    // CREATE NEW LESSON
    // ═══════════════════════════════════════════════════════════
    @PostMapping
    public ResponseEntity<Lesson> create(@RequestBody Lesson lesson) {
        lesson.setId(null);
        normalize(lesson);
        Lesson saved = repo.save(lesson);
        return ResponseEntity.ok(saved);
    }

    // ═══════════════════════════════════════════════════════════
    // UPDATE EXISTING LESSON
    // ═══════════════════════════════════════════════════════════
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
                    existing.setNumberOfLessons(incoming.getNumberOfLessons());
                    existing.setStatus(incoming.getStatus());
                    existing.setSubmittedBy(incoming.getSubmittedBy());
                    existing.setSubLessons(incoming.getSubLessons());

                    normalize(existing);

                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ═══════════════════════════════════════════════════════════
    // DELETE LESSON
    // ═══════════════════════════════════════════════════════════
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ═══════════════════════════════════════════════════════════
    // SYNC LESSONS FROM FRONTEND
    //
    // The frontend sends the full list (approved + pending + rejected)
    // and expects the server to:
    //   1. INSERT lessons with no ID
    //   2. UPDATE lessons with existing IDs
    //   3. DELETE lessons that are no longer in the payload
    //
    // @Transactional ensures all writes happen in one DB transaction
    // and prevents the deadlock from nested repo calls.
    // ═══════════════════════════════════════════════════════════
    @PutMapping("/sync")
    @Transactional
    public List<Lesson> sync(@RequestBody List<Lesson> data) {

        // Empty payload means "delete everything" — but we guard
        // against accidental null calls by returning current state.
        if (data == null) {
            return repo.findAll();
        }

        List<Long> incomingIds = new ArrayList<>();
        List<Lesson> result = new ArrayList<>();

        // ─── 1. UPSERT (insert new + update existing) ─────────
        for (Lesson incoming : data) {

            if (incoming == null) continue;

            // NEW LESSON — no ID yet
            if (incoming.getId() == null) {
                normalize(incoming);
                Lesson saved = repo.save(incoming);
                result.add(saved);
                incomingIds.add(saved.getId());
                continue;
            }

            // EXISTING LESSON — try to find it
            Lesson existing = repo.findById(incoming.getId()).orElse(null);

            if (existing != null) {
                existing.setTitle(incoming.getTitle());
                existing.setCategory(incoming.getCategory());
                existing.setDuration(incoming.getDuration());
                existing.setDescription(incoming.getDescription());
                existing.setNumberOfLessons(incoming.getNumberOfLessons());
                existing.setStatus(incoming.getStatus());
                existing.setSubmittedBy(incoming.getSubmittedBy());
                // ⚠️ Critical: this triggers @PreUpdate → saves to sub_lessons
                existing.setSubLessons(incoming.getSubLessons());

                normalize(existing);

                Lesson saved = repo.save(existing);
                result.add(saved);
                incomingIds.add(saved.getId());
            } else {
                // Frontend sent an ID that no longer exists in DB
                // (e.g. IDs from sessionStorage that were never persisted).
                // Treat it as a NEW insert.
                incoming.setId(null);
                normalize(incoming);
                Lesson saved = repo.save(incoming);
                result.add(saved);
                incomingIds.add(saved.getId());
            }
        }

        // ─── 2. DELETE rows not present in the incoming payload ─
        // Single query instead of a loop — prevents nested
        // transaction deadlocks on MySQL.
        if (incomingIds.isEmpty()) {
            // Nothing to keep — but DON'T nuke the table if the
            // frontend sent an empty array by accident. Only delete
            // if the caller explicitly intended to clear.
            // (If you want empty array = clear all, uncomment below.)
            // repo.deleteAll();
        } else {
            repo.deleteAllByIdNotIn(incomingIds);
        }

        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // DEFAULTS
    // ═══════════════════════════════════════════════════════════
    private void normalize(Lesson lesson) {
        if (lesson.getStatus() == null || lesson.getStatus().isBlank()) {
            lesson.setStatus("approved");
        }
        if (lesson.getSubLessons() == null) {
            lesson.setSubLessons(new ArrayList<>());
        }
    }
}
