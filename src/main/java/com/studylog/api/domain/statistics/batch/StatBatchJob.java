package com.studylog.api.domain.statistics.batch;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.domain.statistics.entity.Stat;
import com.studylog.api.domain.statistics.repository.StatRepository;
import com.studylog.api.domain.timer.entity.Timer;
import com.studylog.api.domain.timer.repository.TimerRepository;
import com.studylog.api.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatBatchJob {

    private final TimerRepository timerRepository;
    private final StatRepository statRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    /**
     * 매일 새벽 4시 실행: 어제(새벽 5시 ~ 오늘 새벽 5시) 학습 시간 집계
     */
    @Scheduled(cron = "0 0 5 * * *")
    @Transactional
    public void calculateDailyStats() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        log.info("=== 일일 통계 집계 시작: {} (기준: 5시~5시) ===", yesterday);

        List<Member> members = memberRepository.findAll();
        int successCount = 0;

        for (Member member : members) {
            try {
                // 중복 체크
                Optional<Stat> existing = statRepository
                        .findByMemberMemberIdAndStatDate(member.getMemberId(), yesterday);

                if (existing.isPresent()) {
                    log.warn("이미 집계된 데이터 스킵: memberId={}, date={}",
                            member.getMemberId(), yesterday);
                    continue;
                }

                // timer에서 어제 기록 조회 (timer_date)
                List<Timer> timers = timerRepository
                        .findAllByMemberIdAndTimerDate(member.getMemberId(), yesterday);

                // duration 합산
                long totalSeconds = timers.stream()
                        .mapToLong(timer -> timer.getDuration().longValue())
                        .sum();

                // statistics 저장 (0초도 저장)
                Stat stat = Stat.builder()
                        .member(member)
                        .statDate(yesterday)
                        .totalStudyTime(totalSeconds)
                        .build();

                statRepository.save(stat);
                successCount++;

                // 통계 알림 전송 (학습 기록이 있는 경우만)
                if (totalSeconds > 0 && member.getMemberEmail() != null) {
                    try {
                        notificationService.createAndSendNotification(
                                member.getMemberEmail(),
                                "STAT",
                                "일일 학습 통계",
                                "어제 총 " + formatTime(totalSeconds) + " 학습했습니다"
                        );
                    } catch (Exception e) {
                        log.warn("통계 알림 전송 실패: memberId={}", member.getMemberId());
                    }
                }

                log.debug("저장 완료: memberId={}, {}초 ({})",
                        member.getMemberId(), totalSeconds, formatTime(totalSeconds));

            } catch (Exception e) {
                log.error("집계 실패: memberId={}", member.getMemberId(), e);
            }
        }

        log.info("=== 일일 통계 집계 완료: 성공={}/{} ===", successCount, members.size());
    }

    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return hours + "시간 " + minutes + "분";
    }
}
