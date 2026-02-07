package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.LikeToggleResult;

public interface CommLikeService {

    //fromUser가 toUser를 좋아요/취소
    LikeToggleResult toggleMemberLike(Long groupId, Long fromUserId, Long toUserId);

    //특정 멤버가 그룹 내에서 받은 좋아요 수
    Long countLikes(Long groupId, Long toUserId);

    //본인이 좋아요 눌렀는지 안눌렀는지
    boolean isLiked(Long groupId, Long fromUserId, Long toUserId);
}
