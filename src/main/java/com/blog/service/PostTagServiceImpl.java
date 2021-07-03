package com.blog.service;

import com.blog.model.PostTag;
import com.blog.model.Tag;
import com.blog.repository.PostTagRepository;
import com.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostTagServiceImpl implements PostTagService {
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostTagServiceImpl(PostTagRepository postTagRepository, TagRepository tagRepository) {
        this.postTagRepository = postTagRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getTagsFromPostId(Long postId) {
        List<Long> tagIds = postTagRepository.findAllTagId(postId);
        List<Tag> tagList = new ArrayList<>();
        for (Long tagId : tagIds) {
            tagList.add(tagRepository.getById(tagId));
        }
        return tagList;
    }

    @Override
    public void savePostWithTags(Long postId, List<Long> tagIdList) {
        for (Long tagId : tagIdList) {
            if (postTagRepository.findByPostAndTagId(postId, tagId) == null) {
                postTagRepository.save(new PostTag(postId, tagId, new Date(), new Date()));
            }
        }
    }
}
