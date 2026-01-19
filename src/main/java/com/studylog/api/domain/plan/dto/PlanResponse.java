package com.studylog.api.domain.plan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="PlanResponse", description = "플래너 정보 응답 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanResponse {
    private Long planId;
}
