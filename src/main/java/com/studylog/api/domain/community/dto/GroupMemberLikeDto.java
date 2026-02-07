package com.studylog.api.domain.community.dto;

public record GroupMemberLikeDto(
        Long memberId,
        String nickname,
        long likeCount,
        boolean likedByMe
) {
}
