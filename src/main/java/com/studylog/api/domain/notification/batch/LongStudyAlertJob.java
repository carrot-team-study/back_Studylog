package com.studylog.api.domain.notification.batch;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class LongStudyAlertJob {

    private final StringRedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    private static final String TIMER_KEY_PREFIX = "timer:";
    private static final String ALERT_KEY_PREFIX = "alert:long_study:";
    private static final long LONG_STUDY_THRESHOLD = 7200; // 2시간(초)

    @Scheduled(fixedRate = 1800000) // 30분마다
    public void checkLongStudySessions() {
        Set<String> timerKeys = redisTemplate.keys(TIMER_KEY_PREFIX + "*");
        if (timerKeys == null || timerKeys.isEmpty()) {
            return;
        }

        for (String key : timerKeys) {
            // timer:pause: 키는 일시정지 상태이므로 스킵
            if (key.startsWith("timer:pause:")) {
                continue;
            }

            try {
                // timer:{memberId}:{subjectId} 형식 파싱
                String[] parts = key.replace(TIMER_KEY_PREFIX, "").split(":");
                if (parts.length != 2) continue;

                Long memberId = Long.parseLong(parts[0]);
                Long subjectId = Long.parseLong(parts[1]);

                // 시작 시간 확인
                String startValue = redisTemplate.opsForValue().get(key);
                if (startValue == null) continue;

                long startEpoch = Long.parseLong(startValue);
                long elapsed = Instant.now().getEpochSecond() - startEpoch;

                if (elapsed < LONG_STUDY_THRESHOLD) continue;

                // 중복 알림 방지: 이미 알림 보냈는지 확인
                String alertKey = ALERT_KEY_PREFIX + memberId + ":" + subjectId;
                if (Boolean.TRUE.equals(redisTemplate.hasKey(alertKey))) continue;

                // 알림 전송
                Member member = memberRepository.findById(memberId).orElse(null);
                if (member != null && member.getMemberEmail() != null) {
                    long hours = elapsed / 3600;
                    long minutes = (elapsed % 3600) / 60;
                    notificationService.createAndSendNotification(
                            member.getMemberEmail(),
                            "LONG_STUDY",
                            "장시간 학습 알림",
                            hours + "시간 " + minutes + "분째 학습 중입니다! 휴식을 취해보세요"
                    );

                    // 중복 알림 방지 키 설정 (2시간 TTL)
                    redisTemplate.opsForValue().set(alertKey, "sent", Duration.ofHours(2));
                    log.info("장시간 학습 알림 전송: memberId={}, elapsed={}초", memberId, elapsed);
                }
            } catch (Exception e) {
                log.warn("장시간 학습 알림 체크 실패: key={}, error={}", key, e.getMessage());
            }
        }
    }
}