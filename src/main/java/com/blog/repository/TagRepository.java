package com.blog.repository;

import com.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(value = "Select id from tag where name = :tagName", nativeQuery = true)
    Long checkTagIsUnique(@Param("tagName") String tagName);
}
