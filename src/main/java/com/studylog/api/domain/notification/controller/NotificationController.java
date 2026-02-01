package com.studylog.api.domain.notification.controller;

import com.studylog.api.domain.notification.dto.NotificationDto;
import com.studylog.api.domain.notification.service.NotificationService;
import com.studylog.api.domain.notification.service.SseEmitterService;
import com.studylog.api.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final SseEmitterService sseEmitterService;
    private final JwtUtil jwtUtil;

    /**
     * SSE 연결 (실시간 알림 구독)
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        return sseEmitterService.createEmitter(email);
    }
    /**
     * 내 알림 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getMyNotifications(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        List<NotificationDto> notifications = notificationService.getMyNotifications(email);
        return ResponseEntity.ok(notifications);
    }
    /**
     * 읽지 않은 알림 개수 조회
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        Long count = notificationService.getUnreadCount(email);
        return ResponseEntity.ok(count);
    }
    /**
     * 알림 읽음 처리
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId,
            @RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        notificationService.markAsRead(notificationId, email);
        return ResponseEntity.ok().build();
    }
    /**
     * 알림 삭제
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            @RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        notificationService.deleteNotification(notificationId, email);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/test")
    public ResponseEntity<Void> sendTestNotification(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        notificationService.createAndSendNotification(
                email,
                "LIKE",
                "테스트 알림",
                "이것은 테스트 알림입니다!"
        );
        return ResponseEntity.ok().build();
    }





}
