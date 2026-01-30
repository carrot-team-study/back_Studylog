package com.studylog.api.domain.notification.entity;

import com.studylog.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "notification_type", nullable = false, length = 20)
    private String notificationType; // 장시간 학습 알림, 친구초대, 통계알림, 좋아요알림

    @Column(name = "notification_title", nullable = false, length = 255)
    private String notificationTitle;

    @Column(name = "notification_content", nullable = false, columnDefinition = "TEXT")
    private String notificationContent;

    @Column(name = "notification_read", nullable = false)
    @Builder.Default
    private Boolean notificationRead = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    public void markAsRead() {
        this.notificationRead = true;
        this.readAt = LocalDateTime.now();
    }
}
