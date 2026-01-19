package com.studylog.api.domain.plan.dto;

import com.studylog.api.domain.plan.entity.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name="PlanRequest", description = "플래너 정보 요청 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanRequest {
    @NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "제목", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    private String content;

    @NotNull(message = "목표날짜는 필수입니다.")
    @Schema(description = "목표 날짜", example = "2026-01-16", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate targetDate;

    @NotNull(message = "시작시간은 필수입니다.")
    @Schema(description = "시작 예정 시간", example = "2026-01-16T22:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @NotNull(message = "종료시간은 필수입니다.")
    @Schema(description = "종료 예정 시간", example = "2026-01-16T22:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    public static Plan toEntity() {

    }
}
