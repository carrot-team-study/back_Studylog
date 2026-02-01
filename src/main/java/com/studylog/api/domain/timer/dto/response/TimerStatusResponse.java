package com.studylog.api.domain.timer.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimerStatusResponse {
    private Long subjectId;
    private boolean running;
    private Long elapsed; // 초단위
}