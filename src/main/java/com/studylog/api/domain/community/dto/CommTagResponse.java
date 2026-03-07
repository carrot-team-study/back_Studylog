package com.studylog.api.domain.community.dto;

import com.studylog.api.domain.community.entity.CommTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommTagResponse {
    private Long tagId;
    private String tagName;

    public static CommTagResponse from(CommTag tag) {
        return new CommTagResponse(tag.getTagId(), tag.getName());
    }
}
