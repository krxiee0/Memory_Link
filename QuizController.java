package com.memorylink.controller;

import com.memorylink.model.Quiz;
import com.memorylink.repository.QuizRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizRepository repo;

    public QuizController(QuizRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Quiz> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Quiz> create(@RequestBody Quiz quiz) {
        quiz.setId(null);
        if (quiz.getQuestionsJson() == null) quiz.setQuestionsJson("[]");
        return ResponseEntity.ok(repo.saveAndFlush(quiz));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> update(@PathVariable Long id, @RequestBody Quiz incoming) {
        return repo.findById(id).map(existing -> {
            if (incoming.getTitle() != null) existing.setTitle(incoming.getTitle());
            if (incoming.getStrand() != null) existing.setStrand(incoming.getStrand());
            if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
            if (incoming.getLesson() != null) existing.setLesson(incoming.getLesson());
            if (incoming.getLessonNumber() != null) existing.setLessonNumber(incoming.getLessonNumber());
            if (incoming.getQuestionsJson() != null) existing.setQuestionsJson(incoming.getQuestionsJson());
            return ResponseEntity.ok(repo.saveAndFlush(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/sync")
    public List<Quiz> sync(@RequestBody List<Quiz> data) {
        for (Quiz incoming : data) {
            if (incoming.getTitle() == null || incoming.getTitle().isBlank()) continue;

            if (incoming.getId() != null && repo.existsById(incoming.getId())) {
                Quiz existing = repo.findById(incoming.getId()).orElseThrow();
                existing.setTitle(incoming.getTitle());
                existing.setStrand(incoming.getStrand());
                existing.setDescription(incoming.getDescription());
                existing.setLesson(incoming.getLesson());
                existing.setLessonNumber(incoming.getLessonNumber());
                if (incoming.getQuestionsJson() != null) existing.setQuestionsJson(incoming.getQuestionsJson());
                repo.saveAndFlush(existing);
            } else {
                incoming.setId(null);
                if (incoming.getQuestionsJson() == null) incoming.setQuestionsJson("[]");
                repo.saveAndFlush(incoming);
            }
        }
        return repo.findAll();
    }
}
