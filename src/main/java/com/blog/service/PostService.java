package com.blog.service;

import com.blog.model.Post;
import com.blog.model.Tag;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    Post savePost(Post post);

    Post getPostByID(Long id);

    void deletePost(Long id);

    Page<Post> findPaginated(int start, int limit, String sortField, String sortDirection);

    List<Post> getPostBYSearchCriteria(String tagName);

    List<Post> filter(List<Tag> tagFilterList, List<Post> authorFilterList, String startDate, String endDate) throws ParseException;
}
