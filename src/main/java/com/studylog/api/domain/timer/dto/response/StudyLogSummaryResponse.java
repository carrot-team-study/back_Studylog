package com.studylog.api.domain.timer.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyLogSummaryResponse {
    private Long totalDuration; // 초 단위
}
