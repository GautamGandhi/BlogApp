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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        comment.setUserId(userRepository.getUserByEmail(authentication.getName()).getId());
        commentService.saveComment(comment);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/deletecomment/{commentId}")
    public String deleteComment(@PathVariable(value = "commentId") Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment comment = commentService.getCommentById(commentId);
        long postId = commentService.getCommentById(commentId).getPostId();
        if(userRepository.getUserByEmail(authentication.getName()).getId() == comment.getUserId()){
            commentService.deleteComment(commentId);
        }
        return "redirect:/post/" + postId;
    }

    @GetMapping("/updatecomment/{commentId}")
    public String updateComment(@PathVariable(value = "commentId") Long commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("commentBind", comment);
        model.addAttribute("id", comment.getPostId());
        return "updatecomment";
    }

    @PostMapping("/updatecomment/{commentId}")
    public String updateComments(@PathVariable(value = "commentId") Long commentId, @ModelAttribute("commentBind")
            Comment userComment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment comment = commentService.getCommentById(commentId);
        userComment.setUserId(comment.getUserId());
        userComment.setCreatedAt(comment.getCreatedAt());
        userComment.setPostId(comment.getPostId());
        userComment.setId(commentId);
        userComment.setUpdatedAt(new Date());
        if(userRepository.getUserByEmail(authentication.getName()).getId() == comment.getUserId()){
            commentService.saveComment(userComment);
        }
        return "redirect:/post/" + userComment.getPostId();
    }
}
