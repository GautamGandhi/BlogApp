package com.blog.service;

import com.blog.model.Tag;

import java.util.List;

public interface PostTagService {

    List<Tag> getTagsFromPostId(Long postID);

    void savePostWithTags(Long id, List<Long> tagIdList);
}
