package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.LikeToggleResult;
import com.studylog.api.domain.community.repository.CommLikeRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommLikeServiceImpl implements CommLikeService{

    private final CommLikeRepository commLikeRepository;

    @Override
    public LikeToggleResult toggleMemberLike(Long groupId, Long fromUserId, Long toUserId) {

        // 자기자신 좋아요 금지
        if (fromUserId.equals(toUserId)) {
            throw new BusinessException(ErrorCode.LIKE_SELF_NOT_ALLOWED);
        }

        // 1) 먼저 삭제 시도 (있으면 취소)
        int deleted = commLikeRepository.deleteLike(groupId, fromUserId, toUserId);
        if (deleted == 1) {
            long likeCount = commLikeRepository.countLikes(groupId, toUserId);
            return new LikeToggleResult(false, likeCount);
        }

        // 2) 없었으면 삽입 (이미 있으면 ON CONFLICT로 무시됨)
        commLikeRepository.insertIgnore(groupId, fromUserId, toUserId);

        long likeCount = commLikeRepository.countLikes(groupId, toUserId);
        return new LikeToggleResult(true, likeCount);

    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Long countLikes(Long groupId, Long toUserId) {
        return commLikeRepository.countLikes(groupId, toUserId);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean isLiked(Long groupId, Long fromUserId, Long toUserId) {
        return commLikeRepository.existsLike(groupId, fromUserId, toUserId);
    }
}
