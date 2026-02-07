package com.studylog.api.domain.community.dto;

public record RankRowDto(
        Long memberId,
        String nickname,
        long score
) {
}
