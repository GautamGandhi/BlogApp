package com.blog.service;

import com.blog.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> viewCommentRelatedToPost(Long postId);

    void saveComment(Comment comment);

    Comment getCommentById(Long commentId);

    void deleteComment(Long commentId);
}
