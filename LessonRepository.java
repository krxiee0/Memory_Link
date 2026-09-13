package com.memorylink.repository;

import com.memorylink.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Delete all lessons whose ID is NOT in the given list.
     * Used by the /sync endpoint to propagate frontend deletions
     * in a single SQL DELETE statement instead of a Java loop
     * (which would open nested transactions and deadlock).
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Lesson l WHERE l.id NOT IN :ids")
    void deleteAllByIdNotIn(@Param("ids") List<Long> ids);
}
