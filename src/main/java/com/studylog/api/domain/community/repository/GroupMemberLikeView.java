package com.studylog.api.domain.community.repository;

public interface GroupMemberLikeView {
    Long getMemberId();
    String getNickname();
    Long getLikeCount();
    Boolean getLikedByMe();
}
