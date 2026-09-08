package com.memorylink.repository;
import com.memorylink.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {}
