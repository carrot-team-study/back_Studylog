package com.studylog.api.domain.notification.service;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.domain.notification.dto.NotificationDto;
import com.studylog.api.domain.notification.entity.Notification;
import com.studylog.api.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final SseEmitterService sseEmitterService;

    /**
     * 알림 생성 및 SSE 전송
     */
    @Transactional
    public void createAndSendNotification(String email, String type, String title, String content) {
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 알림 생성
        Notification notification = Notification.builder()
                .member(member)
                .notificationType(type)
                .notificationTitle(title)
                .notificationContent(content)
                .notificationRead(false)
                .build();

        notificationRepository.save(notification);

        // SSE로 실시간 전송
        NotificationDto dto = NotificationDto.fromEntity(notification);
        sseEmitterService.sendNotification(email, dto);
    }

    /**
     * 내 알림 목록 조회
     */
    public List<NotificationDto> getMyNotifications(String email) {
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        List<Notification> notifications = notificationRepository.findByMemberIdAndNotDeleted(member.getMemberId());

        return notifications.stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 읽지 않은 알림 개수 조회
     */
    public Long getUnreadCount(String email) {
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        return notificationRepository.countUnreadByMemberId(member.getMemberId());
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void markAsRead(Long notificationId, String email) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다"));

        if (!notification.getMember().getMemberEmail().equals(email)) {
            throw new RuntimeException("권한이 없습니다");
        }

        notification.markAsRead();
        notificationRepository.save(notification);
    }

    /**
     * 알림 삭제 (soft delete)
     */
    @Transactional
    public void deleteNotification(Long notificationId, String email) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다"));

        if (!notification.getMember().getMemberEmail().equals(email)) {
            throw new RuntimeException("권한이 없습니다");
        }

        notificationRepository.delete(notification);
    }
}
