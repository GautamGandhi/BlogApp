package com.blog.service;

import com.blog.model.Post;
import com.blog.model.Tag;
import com.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post savePost(Post post) {
        post.setUpdatedAt(new Date());
        post.setPublishedAt(new Date());
        post.setPublished(true);
        post.setCreatedAt(new Date());
        Long userId = 1L;
        post.setUserId(userId);
        return this.postRepository.save(post);
    }

    @Override
    public Post getPostByID(Long id) {
        Optional<Post> optional = postRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException(" Post not found for id:: " + id);
        }
        return optional.get();
    }

    @Override
    public void deletePost(Long id) {
        Optional<Post> optional = postRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException(" Post not found for id:: " + id);
        }
        postRepository.deleteById(id);
    }

    @Override
    public Page<Post> findPaginated(int start, int limit, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(start - 1, limit, sort);
        return this.postRepository.findAll(pageable);
    }

    @Override
    public List<Post> getPostBYSearchCriteria(String criteria) {
        List<Post> postList = postRepository.getPostByTag(criteria);
        postList.addAll(postRepository.getPostByRemainingCriteria(criteria));
        //postRepository.getPostByName(criteria);
        return postList;
    }

    @Override
    public List<Post> filter(List<Tag> tagFilterList, List<Post> authorFilterList, String startDate, String endDate)
            throws ParseException {
        List<Post> postList = new ArrayList<>();
        if (!tagFilterList.isEmpty()) {
            for (Tag tag : tagFilterList) {
                postList.addAll(postRepository.getPostByTag(tag.getId()));
            }
        }
        if (!authorFilterList.isEmpty()) {
            if (postList.isEmpty()) {
                postList.addAll(authorFilterList);
            } else {
                postList.retainAll(authorFilterList);
            }
        }

        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            Date beginDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date finishDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            if (postList.isEmpty()) {
                postList.addAll(postRepository.getPostByDate(beginDate, finishDate));
            } else {
                postList.retainAll(postRepository.getPostByDate(beginDate, finishDate));
            }
        } else if (!startDate.isEmpty()) {
            Date beginDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            if (postList.isEmpty()) {
                postList.addAll(postRepository.getPostByStartDate(beginDate));
            } else {
                postList.retainAll(postRepository.getPostByStartDate(beginDate));
            }
        } else if (!endDate.isEmpty()) {
            Date finishDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            if (postList.isEmpty()) {
                postList.addAll(postRepository.getPostByEndDate(finishDate));
            } else {
                postList.retainAll(postRepository.getPostByEndDate(finishDate));
            }
        }
        return postList;
    }
}

