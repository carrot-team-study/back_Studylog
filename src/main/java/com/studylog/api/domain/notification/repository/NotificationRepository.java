package com.studylog.api.domain.notification.repository;

import com.studylog.api.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

    public interface NotificationRepository extends JpaRepository<Notification, Long> {

        // 회원의 삭제되지 않은 알림 조회 (최신순)
        @Query("SELECT n FROM Notification n WHERE n.member.memberId = :memberId AND n.deletedAt IS NULL ORDER BY n.createdAt DESC")
        List<Notification> findByMemberIdAndNotDeleted(@Param("memberId") Long memberId);

        // 회원의 읽지 않은 알림 개수
        @Query("SELECT COUNT(n) FROM Notification n WHERE n.member.memberId = :memberId AND n.notificationRead = false AND n.deletedAt IS NULL")
        Long countUnreadByMemberId(@Param("memberId") Long memberId);

        // 회원의 읽지 않은 알림 조회
        @Query("SELECT n FROM Notification n WHERE n.member.memberId = :memberId AND n.notificationRead = false AND n.deletedAt IS NULL ORDER BY n.createdAt DESC")
        List<Notification> findUnreadByMemberId(@Param("memberId") Long memberId);
    }
