package com.memorylink.repository;

import com.memorylink.model.Strand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StrandRepository extends JpaRepository<Strand, Long> {
    Optional<Strand> findByName(String name);
}
