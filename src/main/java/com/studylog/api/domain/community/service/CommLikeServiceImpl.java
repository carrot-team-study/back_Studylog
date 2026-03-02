package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.LikeToggleResult;
import com.studylog.api.domain.community.repository.CommLikeRepository;
import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.domain.notification.service.NotificationService;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommLikeServiceImpl implements CommLikeService{

    private final CommLikeRepository commLikeRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

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

        // 좋아요 알림 전송
        try {
            Member fromUser = memberRepository.findById(fromUserId)
                    .orElse(null);
            Member toUser = memberRepository.findById(toUserId)
                    .orElse(null);
            if (fromUser != null && toUser != null && toUser.getMemberEmail() != null) {
                notificationService.createAndSendNotification(
                        toUser.getMemberEmail(),
                        "LIKE",
                        "좋아요 알림",
                        fromUser.getMemberNickname() + "님이 좋아요를 눌렀습니다"
                );
            }
        } catch (Exception e) {
            log.warn("좋아요 알림 전송 실패: {}", e.getMessage());
        }

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