package com.memorylink.controller;

import com.memorylink.model.Strand;
import com.memorylink.repository.StrandRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/strands")
public class StrandController {
    private final StrandRepository repo;

    public StrandController(StrandRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Strand> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Strand> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Strand> create(@RequestBody Strand strand) {
        strand.setId(null);
        normalize(strand);
        return ResponseEntity.ok(repo.save(strand));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Strand> update(@PathVariable Long id, @RequestBody Strand incoming) {
        return repo.findById(id).map(existing -> {
            if (incoming.getName() != null && !incoming.getName().isBlank()) existing.setName(incoming.getName());
            if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
            if (incoming.getIcon() != null && !incoming.getIcon().isBlank()) existing.setIcon(incoming.getIcon());
            if (incoming.getStatus() != null && !incoming.getStatus().isBlank()) existing.setStatus(incoming.getStatus());
            normalize(existing);
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
    public List<Strand> sync(@RequestBody List<Strand> data) {
        for (Strand incoming : data) {
            if (incoming.getName() == null || incoming.getName().isBlank()) continue;

            Strand existing = repo.findByName(incoming.getName()).orElse(null);
            if (existing != null) {
                existing.setDescription(incoming.getDescription());
                existing.setIcon(incoming.getIcon());
                existing.setStatus(incoming.getStatus());
                normalize(existing);
                repo.save(existing);
            } else {
                incoming.setId(null);
                normalize(incoming);
                repo.save(incoming);
            }
        }
        return repo.findAll();
    }

    private void normalize(Strand strand) {
        if (strand.getIcon() == null || strand.getIcon().isBlank()) strand.setIcon("fa-book-open");
        if (strand.getStatus() == null || strand.getStatus().isBlank()) strand.setStatus("active");
        strand.setStatus(strand.getStatus().trim().toLowerCase());
    }
}
