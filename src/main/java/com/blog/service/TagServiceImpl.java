package com.blog.service;

import com.blog.model.Tag;
import com.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {


    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Long> saveTags(Set<String> tagSet) {
        List<Long> tagIdList = new ArrayList<>();
        for (String tag : tagSet) {
            Long tagId = tagRepository.checkTagIsUnique(tag.trim().toLowerCase(Locale.ROOT));
            if (tagId == null) {
                tagIdList.add(tagRepository.save(new Tag(tag.trim().toLowerCase(Locale.ROOT), new Date(),
                        new Date())).getId());
            } else {
                tagIdList.add(tagId);
            }
        }
        return tagIdList;
    }

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }
}
