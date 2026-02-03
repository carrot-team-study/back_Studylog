package com.studylog.api.domain.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Schema(description = "과목별 통계 DTO")
@Alias("StatSubjectDto")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatSubjectDto {

    @Schema(description = "과목 이름", example = "자바")
    private String subjectName;

    @Schema(description = "과목별 공부 시간 (초)", example = "2400")
    private Long totalStudyTime;

    @Schema(description = "과목별 비중 (%)", example = "44.4")
    private Double ratio;
}
