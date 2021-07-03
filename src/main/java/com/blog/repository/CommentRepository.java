package com.blog.repository;

import com.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * from comment where post_id = ?1", nativeQuery = true)
    List<Comment> findAllCommentForParticularPost(Long postId);
}
