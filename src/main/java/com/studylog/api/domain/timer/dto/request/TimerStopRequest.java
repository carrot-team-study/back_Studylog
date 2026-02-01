package com.studylog.api.domain.timer.dto.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class TimerStopRequest {
    @Column(nullable = false)
    private Long subjectId;
}
