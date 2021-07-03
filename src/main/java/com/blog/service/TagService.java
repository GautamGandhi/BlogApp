package com.blog.service;

import com.blog.model.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<Long> saveTags(Set<String> tagSet);

    List<Tag> getTags();
}
