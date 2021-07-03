package com.blog.service;

import com.blog.model.Comment;
import com.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> viewCommentRelatedToPost(Long postId) {
        return commentRepository.findAllCommentForParticularPost(postId);
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        if (optional.isEmpty()) {
            throw new RuntimeException(" Post not found for id:: " + commentId);
        }
        return optional.get();
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
