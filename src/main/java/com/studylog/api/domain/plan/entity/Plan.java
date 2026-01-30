package com.studylog.api.domain.plan.entity;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name="plan_info")
public class Plan extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false, length = 20)
    private String title;

    private String content;

    @Column(nullable = false)
    private LocalDate targetDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateInfo(String title, String content, LocalDate targetDate,
                           LocalTime startTime, LocalTime endTime) {
        this.title = title;
        this.content = content;
        this.targetDate = targetDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}