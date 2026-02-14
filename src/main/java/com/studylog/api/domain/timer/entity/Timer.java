package com.studylog.api.domain.timer.entity;

import com.studylog.api.domain.subject.entity.Subject;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "timer",
        indexes = {
                @Index(name = "idx_timer_member_date", columnList = "member_id, timer_date")
        })
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timerId;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(nullable = false)
    private Long memberId;

    // 초단위로 기록
    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private LocalDate timerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private Subject subject;

    public Timer(Long subjectId, Long memberId, LocalDateTime startTime, LocalDateTime endTime, Long duration, LocalDate timerDate) {
        this.subjectId = subjectId;
        this.memberId = memberId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.timerDate = timerDate;
    }
}
