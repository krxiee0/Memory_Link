package com.memorylink.controller;

import com.memorylink.model.Flashcard;
import com.memorylink.repository.FlashcardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {
    private final FlashcardRepository repo;

    public FlashcardController(FlashcardRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Flashcard> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flashcard> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Flashcard> create(@RequestBody Flashcard card) {
        card.setId(null);
        return ResponseEntity.ok(repo.save(card));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flashcard> update(@PathVariable Long id, @RequestBody Flashcard incoming) {
        return repo.findById(id).map(existing -> {
            if (incoming.getStrand() != null) existing.setStrand(incoming.getStrand());
            if (incoming.getDifficulty() != null) existing.setDifficulty(incoming.getDifficulty());
            if (incoming.getQuestion() != null) existing.setQuestion(incoming.getQuestion());
            if (incoming.getAnswer() != null) existing.setAnswer(incoming.getAnswer());
            if (incoming.getLesson() != null) existing.setLesson(incoming.getLesson());
            if (incoming.getLessonNumber() != null) existing.setLessonNumber(incoming.getLessonNumber());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/sync")
    public List<Flashcard> sync(@RequestBody List<Flashcard> data) {
        for (Flashcard incoming : data) {
            if (incoming.getQuestion() == null || incoming.getQuestion().isBlank()) continue;

            if (incoming.getId() != null && repo.existsById(incoming.getId())) {
                Flashcard existing = repo.findById(incoming.getId()).orElseThrow();
                existing.setStrand(incoming.getStrand());
                existing.setDifficulty(incoming.getDifficulty());
                existing.setQuestion(incoming.getQuestion());
                existing.setAnswer(incoming.getAnswer());
                existing.setLesson(incoming.getLesson());
                existing.setLessonNumber(incoming.getLessonNumber());
                repo.save(existing);
            } else {
                incoming.setId(null);
                repo.save(incoming);
            }
        }
        return repo.findAll();
    }
}
