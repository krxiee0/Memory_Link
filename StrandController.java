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

    public StrandController(StrandRepository repo) {
        this.repo = repo;
    }

    /* =========================================
       GET ALL STRANDS
    ========================================== */
    @GetMapping
    public List<Strand> all() {
        return repo.findAll();
    }


    /* =========================================
       GET ONE STRAND
    ========================================== */
    @GetMapping("/{id}")
    public ResponseEntity<Strand> one(@PathVariable Long id) {

        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /* =========================================
       CREATE STRAND
    ========================================== */
    @PostMapping
    public ResponseEntity<Strand> create(
            @RequestBody Strand strand) {

        // Let MySQL generate the ID
        strand.setId(null);

        normalize(strand);

        Strand saved = repo.save(strand);

        return ResponseEntity.ok(saved);
    }


    /* =========================================
       UPDATE STRAND
    ========================================== */
    @PutMapping("/{id}")
    public ResponseEntity<Strand> update(
            @PathVariable Long id,
            @RequestBody Strand incoming) {

        return repo.findById(id)
                .map(existing -> {

                    if (incoming.getName() != null
                            && !incoming.getName().isBlank()) {

                        existing.setName(
                                incoming.getName()
                        );
                    }

                    if (incoming.getDescription() != null) {

                        existing.setDescription(
                                incoming.getDescription()
                        );
                    }

                    if (incoming.getIcon() != null
                            && !incoming.getIcon().isBlank()) {

                        existing.setIcon(
                                incoming.getIcon()
                        );
                    }

                    if (incoming.getStatus() != null
                            && !incoming.getStatus().isBlank()) {

                        existing.setStatus(
                                incoming.getStatus()
                        );
                    }

                    normalize(existing);

                    Strand saved = repo.save(existing);

                    return ResponseEntity.ok(saved);

                })
                .orElse(ResponseEntity.notFound().build());
    }


    /* =========================================
       DELETE STRAND
       DIRECT DATABASE DELETE
    ========================================== */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        // Check if strand exists
        if (!repo.existsById(id)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        // Delete directly from MySQL
        repo.deleteById(id);

        // 204 = successfully deleted
        return ResponseEntity
                .noContent()
                .build();
    }


    /* =========================================
       SYNC STRANDS
    ========================================== */
    @PutMapping("/sync")
    public List<Strand> sync(
            @RequestBody List<Strand> data) {

        for (Strand incoming : data) {

            // Ignore empty strand names
            if (incoming.getName() == null
                    || incoming.getName().isBlank()) {

                continue;
            }

            Strand existing =
                    repo.findByName(
                            incoming.getName()
                    ).orElse(null);

            if (existing != null) {

                existing.setDescription(
                        incoming.getDescription()
                );

                existing.setIcon(
                        incoming.getIcon()
                );

                existing.setStatus(
                        incoming.getStatus()
                );

                normalize(existing);

                repo.save(existing);

            } else {

                // Let MySQL generate ID
                incoming.setId(null);

                normalize(incoming);

                repo.save(incoming);
            }
        }

        return repo.findAll();
    }


    /* =========================================
       NORMALIZE STRAND DATA
    ========================================== */
    private void normalize(Strand strand) {

        if (strand.getIcon() == null
                || strand.getIcon().isBlank()) {

            strand.setIcon("fa-book-open");
        }

        if (strand.getStatus() == null
                || strand.getStatus().isBlank()) {

            strand.setStatus("active");
        }

        strand.setStatus(
                strand.getStatus()
                        .trim()
                        .toLowerCase()
        );
    }
}
