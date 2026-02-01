package com.studylog.api.domain.notification.dto;

import com.studylog.api.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long notificationId;
    private String notificationType;
    private String notificationTitle;
    private String notificationContent;
    private Boolean notificationRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    public static NotificationDto fromEntity(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .notificationType(notification.getNotificationType())
                .notificationTitle(notification.getNotificationTitle())
                .notificationContent(notification.getNotificationContent())
                .notificationRead(notification.getNotificationRead())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .build();
    }
}
