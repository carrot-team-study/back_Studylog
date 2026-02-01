package com.studylog.api.domain.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SseEmitterService {
    // 회원 이메일을 키로 SseEmitter 관리
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // SSE 연결 타임아웃 (30분)
    private static final Long TIMEOUT = 30 * 60 * 1000L;

    /**
     * SSE 연결 생성
     */
    public SseEmitter createEmitter(String email) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.put(email, emitter);

        // 연결 완료 시 emitter 제거
        emitter.onCompletion(() -> {
            log.info("SSE 연결 완료: {}", email);
            emitters.remove(email);
        });

        // 타임아웃 시 emitter 제거
        emitter.onTimeout(() -> {
            log.info("SSE 타임아웃: {}", email);
            emitters.remove(email);
        });

        // 에러 발생 시 emitter 제거
        emitter.onError((e) -> {
            log.error("SSE 에러: {}", email, e);
            emitters.remove(email);
        });

        // 연결 즉시 더미 데이터 전송 (503 에러 방지)
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected"));
        } catch (IOException e) {
            log.error("SSE 연결 실패: {}", email, e);
            emitters.remove(email);
        }

        log.info("SSE 연결 생성: {}", email);
        return emitter;
    }

    /**
     * 특정 사용자에게 알림 전송
     */
    public void sendNotification(String email, Object data) {
        SseEmitter emitter = emitters.get(email);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(data));
                log.info("알림 전송 성공: {}", email);
            } catch (IOException e) {
                log.error("알림 전송 실패: {}", email, e);
                emitters.remove(email);
            }
        } else {
            log.warn("연결된 SSE 없음: {}", email);
        }
    }

    /**
     * SSE 연결 제거
     */
    public void removeEmitter(String email) {
        emitters.remove(email);
        log.info("SSE 연결 제거: {}", email);
    }

}
