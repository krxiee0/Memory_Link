package com.memorylink.repository;

import com.memorylink.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository
        extends JpaRepository<Lesson, Long> {
}
