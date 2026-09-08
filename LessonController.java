package com.memorylink.controller;
import com.memorylink.model.Lesson;
import com.memorylink.repository.LessonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/lessons")
public class LessonController {
 private final LessonRepository repo; public LessonController(LessonRepository repo){this.repo=repo;}
 @GetMapping public List<Lesson> all(){return repo.findAll();}
 @GetMapping("/{id}") public ResponseEntity<Lesson> one(@PathVariable Long id){return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
 @PostMapping public ResponseEntity<Lesson> create(@RequestBody Lesson l){l.setId(null);if(l.getStatus()==null)l.setStatus("approved");return ResponseEntity.ok(repo.save(l));}
 @PutMapping("/{id}") public ResponseEntity<Lesson> update(@PathVariable Long id,@RequestBody Lesson l){if(!repo.existsById(id))return ResponseEntity.notFound().build();l.setId(id);return ResponseEntity.ok(repo.save(l));}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){if(!repo.existsById(id))return ResponseEntity.notFound().build();repo.deleteById(id);return ResponseEntity.noContent().build();}
 @PutMapping("/sync") public List<Lesson> sync(@RequestBody List<Lesson> data){repo.deleteAllInBatch();data.forEach(l->{l.setId(null);if(l.getStatus()==null)l.setStatus("approved");});return repo.saveAll(data);}
}