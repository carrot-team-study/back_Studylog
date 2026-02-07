package com.studylog.api.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record LikeToggleResult(

        boolean liked,
        long likeCount

) {}
