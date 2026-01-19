package com.studylog.api.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name="PLAN_INFO")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long planId;
    private Long memberId;
    private String title;
    private String content;
    private LocalDate targetDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isCompleted;
}
