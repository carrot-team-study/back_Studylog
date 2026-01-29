package com.studylog.api.domain.plan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.plan.entity.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(name="PlanResponseDto", description = "플래너 정보 응답 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlanResponseDto {

    @Schema(description = "플랜 ID")
    private Long planId;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "목표 날짜")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

    @Schema(description = "완료 여부")
    private boolean isCompleted;

    @Schema(description = "시작 시간")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(description = "종료 시간")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간")
    private LocalDateTime updatedAt;

    public static PlanResponseDto from(Plan plan) {
        return PlanResponseDto.builder()
                .planId(plan.getPlanId())
                .title(plan.getTitle())
                .content(plan.getContent())
                .targetDate(plan.getTargetDate())
                .startTime(plan.getStartTime())
                .endTime(plan.getEndTime())
                .isCompleted(plan.isCompleted())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}
