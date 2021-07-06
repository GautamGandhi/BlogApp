package com.blog.controller;

import com.blog.model.Comment;
import com.blog.repository.UserRepository;
import com.blog.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private final CommentServiceImpl commentService;
    private final UserRepository userRepository;

    public CommentController(CommentServiceImpl commentService, UserRepository userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/savecomment/{id}")
    public String saveComment(@ModelAttribute("commentBinding") Comment comment,
                              @PathVariable(value = "id") Long postId) {
        comment.setPostId(postId);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        commentService.saveComment(comment);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/deletecomment/{commentId}")
    public String deleteComment(@PathVariable(value = "commentId") Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment comment = commentService.getCommentById(commentId);
        if(authentication.getName().equals(comment.getEmail())){
            commentService.deleteComment(commentId);
        }
        return "redirect:/post/" + comment.getPostId();
    }

    @GetMapping("/updatecomment/{commentId}")
    public String updateComment(@PathVariable(value = "commentId") Long commentId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment comment = commentService.getCommentById(commentId);
        if(authentication.getName().equals(comment.getEmail())){
            model.addAttribute("commentBind", comment);
            model.addAttribute("id", comment.getPostId());
            return "updatecomment";
        }
        return "redirect:/post/" + comment.getPostId();
    }

    @PostMapping("/updatecomment/{commentId}")
    public String updateComments(@PathVariable(value = "commentId") Long commentId, @ModelAttribute("commentBind")
            Comment userComment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment comment = commentService.getCommentById(commentId);
        userComment.setCreatedAt(comment.getCreatedAt());
        userComment.setPostId(comment.getPostId());
        userComment.setId(commentId);
        userComment.setUpdatedAt(new Date());
        if(authentication.getName().equals(comment.getEmail())){
            commentService.saveComment(userComment);
        }
        return "redirect:/post/" + userComment.getPostId();
    }
}
