package com.blog.controller;

import com.blog.model.Comment;
import com.blog.model.Filter;
import com.blog.model.Post;
import com.blog.model.User;
import com.blog.service.*;
import com.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PostController {

    public static final int LIMIT = 5;
    public static final int PAGE_NO = 1;

    private final PostServiceImpl postService;
    private final CommentServiceImpl commentService;
    private final TagServiceImpl tagService;
    private final PostTagServiceImpl postTagService;
    private final UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public PostController(PostServiceImpl postService, CommentServiceImpl commentService, TagServiceImpl tagService,
                          PostTagServiceImpl postTagService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.postTagService = postTagService;
        this.userService = userService;
    }

    @GetMapping(path = "/")
    public String getAllPost(Model model) {
        return pagination(PAGE_NO, "publishedAt", "asc", model);
    }

    @GetMapping("/newpost")
    public String getNewPostForm(Model model) {
        Set<String> roleSet = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            roleSet = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
        }
        String userEmail = authentication.getName();
        Post post = new Post();
        assert roleSet != null;
        if (roleSet.contains("USER")) {
            model.addAttribute("role", "USER");
            post.setAuthor(userService.getName(userEmail));
        }
        model.addAttribute("post", post);
        return "newpost";
    }

    @PostMapping("/savepost")
    public String savePost(@ModelAttribute("post") Post post, @RequestParam(value = "tag", required = false) String tags
            , Model model) {
        post = postService.savePost(post);
        Set<String> tagSet = new HashSet<>(Arrays.asList(tags.split(",")));
        List<Long> tagIdList = tagService.saveTags(tagSet);
        postTagService.savePostWithTags(post.getId(), tagIdList);
        return "redirect:/";
    }

    @PostMapping("/savepost/{id}")
    public String savePost(@PathVariable(value = "id") java.lang.Long id, @ModelAttribute("post") Post post) {
        post.setUpdatedAt(new Date());
        postService.savePost(post);
        return "redirect:/post/" + id;
    }

    @GetMapping("/deletepost/{id}")
    public String deletePost(@PathVariable(value = "id") Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String readPost(@PathVariable(value = "id") Long postId, Model model) {
        model.addAttribute("post", postService.getPostByID(postId));
        model.addAttribute("comments", commentService.viewCommentRelatedToPost(postId));
        Comment comment = new Comment();
        model.addAttribute("comment", comment);
        model.addAttribute("tags", postTagService.getTagsFromPostId(postId));
        return "viewpost";
    }

    @GetMapping("/updatepost/{id}")
    public String updatePost(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("post", postService.getPostByID(id));
        return "updatepost";
    }

    @GetMapping("/page/{start}")
    public String pagination(@PathVariable(value = "start") int pageNo,
                             @RequestParam("sortField") String sortField,
                             @RequestParam("sortDir") String sortDir,
                             Model model) {
        Page<Post> page = postService.findPaginated(pageNo, LIMIT, sortField, sortDir);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listPosts", page.getContent());
        model.addAttribute("tagFilter", tagService.getTags());
        model.addAttribute("authorsFilter", postService.getAllPosts());
        model.addAttribute("filter", new Filter());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Post> postsList = postService.getPostBYSearchCriteria(search);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", 1);
        model.addAttribute("sortField", "");
        model.addAttribute("sortDir", "");
        model.addAttribute("reverseSortDir", "");
        model.addAttribute("listPosts", postsList);
        model.addAttribute("tagFilter", tagService.getTags());
        model.addAttribute("authorsFilter", postService.getAllPosts());
        model.addAttribute("filter", new Filter());
        return "index";
    }

    @PostMapping("/filter")
    public String filter(@ModelAttribute("filter") Filter filter, Model model) throws ParseException {
        List<Post> postsList = postService.filter(filter.getTagFilterList(), filter.getAuthorFilterList(),
                filter.getStartDate(), filter.getEndDate());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", 1);
        model.addAttribute("sortField", "");
        model.addAttribute("sortDir", "");
        model.addAttribute("reverseSortDir", "");
        model.addAttribute("listPosts", postsList);
        model.addAttribute("tagFilter", tagService.getTags());
        model.addAttribute("authorsFilter", postService.getAllPosts());
        model.addAttribute("filter", new Filter());
        return "index";
    }

    @RequestMapping("/saveadmin")
    @ResponseBody
    public User saveAdmin(){
        User user = new User();
        user.setEmail("gautam.gandhi.100@gmail.com");
        user.setPassword(passwordEncoder.encode("123"));
        user.setName("Gautam");
        user.setRole("ADMIN");
        return userRepository.save(user);
    }

    @RequestMapping("/saveuser")
    @ResponseBody
    public User saveUser(){
        User user = new User();
        user.setEmail("manish@gmail.com");
        user.setPassword(passwordEncoder.encode("123"));
        user.setName("Manish");
        user.setRole("USER");
        return userRepository.save(user);
    }
}


