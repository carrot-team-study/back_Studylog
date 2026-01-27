package com.studylog.api.domain.timer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "timer")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timerId;

    @Column(nullable = false)
    private Long subjectId;

    @Column(nullable = false)
    private Long memberId;

    // 초단위로 기록
    @Column(nullable = false)
    private BigDecimal duration;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private LocalDate timerDate;

    public Timer(
            Long subjectId,
            Long memberId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            BigDecimal duration,
            LocalDate timerDate
    ) {
        this.subjectId = subjectId;
        this.memberId = memberId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.timerDate = timerDate;
    }
}
