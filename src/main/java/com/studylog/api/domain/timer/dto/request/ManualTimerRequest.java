package com.studylog.api.domain.timer.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManualTimerRequest {
    @NotNull
    private Long subjectId;

    @NotNull
    private Long memberId;

    @Min(1)
    private Long duration;
}
