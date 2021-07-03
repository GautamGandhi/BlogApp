package com.blog.repository;

import com.blog.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Query(value = "SELECT tag_id from post_tag where post_id = :postId", nativeQuery = true)
    List<Long> findAllTagId(@Param("postId") Long postId);

    @Query(value = "Select id from post_tag where post_id = :postId AND tag_id = :tagId", nativeQuery = true)
    Long findByPostAndTagId(@Param("postId") Long postId, @Param("tagId") Long tagId);
}
