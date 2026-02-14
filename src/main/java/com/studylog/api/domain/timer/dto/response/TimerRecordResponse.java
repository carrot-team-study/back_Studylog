package com.studylog.api.domain.timer.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TimerRecordResponse {
    private Long timerId;
    private Long subjectId;
    private String subjectName;
    private Long duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate timerDate;
}
