package com.studylog.api.domain.timer.service;

import com.studylog.api.domain.timer.dto.request.ManualTimerRequest;
import com.studylog.api.domain.timer.dto.response.StudyLogSummaryResponse;
import com.studylog.api.domain.timer.dto.response.TimerStatusResponse;
import com.studylog.api.domain.timer.dto.response.TimerStopResponse;
import com.studylog.api.domain.timer.entity.Timer;
import com.studylog.api.domain.timer.repository.TimerRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TimerService {
    private final StringRedisTemplate redisTemplate;
    private final TimerRepository timerRepository;

    // Key
    private static final String TIMER_KEY_PREFIX = "timer:";
    private static final String PAUSE_KEY_PREFIX = "timer:pause:";

    // Redis Key 생성 공통 메소드
    private String timerKey(Long memberId, Long subjectId) {
        return TIMER_KEY_PREFIX + memberId + ":" + subjectId;
    }

    private String pauseKey(Long memberId, Long subjectId) {
        return PAUSE_KEY_PREFIX + memberId + ":" + subjectId;
    }

    // TTL
    private static final Duration TIMER_TTL = Duration.ofHours(8);

    // 타이머 시작
    public void start(Long memberId, Long subjectId) {
        // Key 생성 : Redis에 저장할 고유한 식별자
        String timerKey = timerKey(memberId, subjectId);
        String pauseRedisKey = pauseKey(memberId, subjectId);

        // 일시정지된 상태에서 시작 버튼을 누른다면
        if (redisTemplate.opsForValue().get(pauseRedisKey) != null) {
            throw new BusinessException(ErrorCode.TIMER_ALREADY_PAUSED);
        }

        /*
        * setIfAbsent 란?
        *   Key 가 없다면 data 저장 후 true 반환, 이미 존재한다면 false 반환 = 방어막 역할
        * Instant.now().getEpochSecond() : 현재 시간을 초 단위로 저장
        * */
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(
                        timerKey,
                        String.valueOf(Instant.now().getEpochSecond()),
                        TIMER_TTL
                );

        // 중복 확인
        if(Boolean.FALSE.equals(success)){
            throw new BusinessException(ErrorCode.TIMER_ALREADY_RUNNING);
        }
    }

    // 타이머 일시정지
    public void pause(Long memberId, Long subjectId) {
        String timerKey = timerKey(memberId, subjectId);
        String pauseRedisKey = pauseKey(memberId, subjectId);

        // 현재 경과 시간 확인
        String startValue = redisTemplate.opsForValue().get(timerKey);
        // 일시정지 데이터 존재 여부 확인
        String pausedValue = redisTemplate.opsForValue().get(pauseRedisKey);
        // 이미 일시정지 상태라면 예외 발생
        if (pausedValue != null) {
            throw new BusinessException(ErrorCode.TIMER_ALREADY_PAUSED);
        }
        // 실행 중인 타이머가 없다면 예외 발생
        if (startValue == null) {
            throw new BusinessException(ErrorCode.TIMER_NOT_RUNNING);
        }
        // 현재 시점까지의 총 경과 시간(초 단위) 계산
        long elapsed = Instant.now().getEpochSecond() - Long.parseLong(startValue);
        // 실행 상태 삭제 후, 경과 시간만 별도 저장
        redisTemplate.delete(timerKey);
        redisTemplate.opsForValue().set(pauseRedisKey, String.valueOf(elapsed), TIMER_TTL);
    }

    // 타이머 재개
    public void resume(Long memberId, Long subjectId) {
        String pauseRedisKey = pauseKey(memberId, subjectId);
        String timerKey = timerKey(memberId, subjectId);
        // 일시정지 상태일 때 저장했던 경과 시간 확인
        String elapsedValue = redisTemplate.opsForValue().get(pauseRedisKey);
        // 일시정지가 아닌 상태에서 재개 버튼을 누른다면
        if (elapsedValue == null) {
            throw new BusinessException(ErrorCode.TIMER_NOT_RUNNING);
        }
        // 이미 실행 중인 상태에서 재개 버튼을 누른다면
        String startValue = redisTemplate.opsForValue().get(timerKey);
        if (startValue != null) {
            throw new BusinessException(ErrorCode.TIMER_ALREADY_RUNNING);
        }

        long elapsed = Long.parseLong(elapsedValue);
        // 가상의 시작 시간 계산 : 현재 시간 - 이전 경과 시간 (이어서 측정하는 효과)
        long newStart = Instant.now().getEpochSecond() - elapsed;
        // 실행 상태로 복구하고 일시정지 기록 삭제
        redisTemplate.delete(pauseRedisKey);
        redisTemplate.opsForValue().set(timerKey, String.valueOf(newStart), TIMER_TTL);
    }

    // 타이머 종료
    public TimerStopResponse stop(Long memberId, Long subjectId) {
        String timerKey = timerKey(memberId, subjectId);
        String pauseRedisKey = pauseKey(memberId, subjectId);

        // 시작 시간 확인
        String startValue = redisTemplate.opsForValue().get(timerKey);
        String pausedValue = redisTemplate.opsForValue().get(pauseRedisKey);

        if (startValue == null && pausedValue == null) {
            throw new BusinessException(ErrorCode.TIMER_NOT_RUNNING);
        }

        // 총 공부한 시간 초단위
        long durationSeconds;
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime;

        if (pausedValue != null) {
            // 일시정지 상태에서 종료
            durationSeconds = Long.parseLong(pausedValue);
            startTime = endTime.minusSeconds(durationSeconds);
            redisTemplate.delete(pauseRedisKey);
        } else {
            // 실행 중 상태에서 종료
            // Redis 에서 가져온 시작 시간
            long startEpoch = Long.parseLong(startValue);
            // 현재 종료 시간
            durationSeconds = Instant.now().getEpochSecond() - startEpoch;
            startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(startEpoch), ZoneId.systemDefault());

            // Redis 임시 data 삭제
            // 삭제 이유 : 해당 임시 data를 삭제해야 다음번 다시 start를 호출 시 setIfAbsent가 성공해 새 타이머 시작 가능
            redisTemplate.delete(timerKey);
        }

        // 실제 DB 에 저장하기 위해 Entity 생성
        Timer timer = new Timer(
                subjectId,
                memberId,
                startTime,
                endTime,
                BigDecimal.valueOf(durationSeconds),
                startTime.toLocalDate()
        );
        timerRepository.save(timer);

        return TimerStopResponse.builder()
                .duration(durationSeconds)
                .build();
    }

    // 타이머 상태 조회
    @Transactional(readOnly = true)
    public TimerStatusResponse status(Long memberId, Long subjectId) {
        String timerKey = timerKey(memberId, subjectId);
        String pauseRedisKey = pauseKey(memberId, subjectId);

        // 일시정지 상태일 때 저장했던 경과 시간 확인
        String pausedValue = redisTemplate.opsForValue().get(pauseRedisKey);
        // 만일 타이머가 일시정지 상태가 아니라면
        if (pausedValue != null) {
            return TimerStatusResponse.builder()
                    .subjectId(subjectId)
                    .running(false)
                    .elapsed(Long.parseLong(pausedValue))
                    .build();
        }

        // 시작 시간 확인
        String startValue = redisTemplate.opsForValue().get(timerKey);
        // 만일 타이머를 작동하지 않았다면
        if (startValue == null) {
            return TimerStatusResponse.builder()
                    .subjectId(subjectId)
                    .running(false)
                    .elapsed(0L)
                    .build();
        }

        // 만일 타이머가 작동하고 있다면
        long startEpoch = Long.parseLong(startValue);
        long elapsed = Instant.now().getEpochSecond() - startEpoch;

        return TimerStatusResponse.builder()
                .subjectId(subjectId)
                .running(true)
                .elapsed(elapsed)
                .build();
    }

    // 해당 사용자의 해당 날짜에 대한 타이머 내역 조회
    @Transactional(readOnly = true)
    public List<Timer> getRecords(Long memberId, LocalDate date) {
        return timerRepository.findAllByMemberIdAndTimerDate(memberId, date);
    }

    // 수동 학습 기록
    public TimerStopResponse addManualRecord(ManualTimerRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Timer timer = new Timer(
                request.getSubjectId(),
                request.getMemberId(),
                now.minusSeconds(request.getDuration()), // 시작 시간 = 현재 - duration
                now,
                BigDecimal.valueOf(request.getDuration()),
                now.toLocalDate()
        );
        timerRepository.save(timer);

        return TimerStopResponse.builder()
                .duration(request.getDuration())
                .build();
    }

    // 하루 총 학습 시간 요약
    @Transactional(readOnly = true)
    public StudyLogSummaryResponse getDailySummary(Long memberId, LocalDate date) {
        List<Timer> timers = timerRepository.findAllByMemberIdAndTimerDate(memberId, date);
        long totalSeconds = timers.stream()
                .map(Timer::getDuration)
                .mapToLong(BigDecimal::longValue)
                .sum();

        return StudyLogSummaryResponse.builder()
                .totalDuration(totalSeconds)
                .build();
    }
}