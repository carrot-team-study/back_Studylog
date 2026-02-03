package com.studylog.api.domain.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Schema(description = "통계 통합 DTO")
@Alias("StatDto")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {

    @Schema(description = "통계 유형", example = "DAILY / WEEKLY / MONTHLY")
    private String periodType;

    @Schema(description = "통계 날짜", example = "2026-01-30")
    private LocalDate startDate;

    @Schema(description = "총 공부 시간 (초)", example = "3600")
    private Long totalStudyTime;

    @Schema(description = "과목 이름", example = "자바")
    private String subjectName;

    @Schema(description = "과목별 비중 (%)", example = "45.5")
    private Double ratio;

    @Schema(description = "가공된 시간 표현", example = "1시간 0분")
    public String getDisplayTime() {
        if (totalStudyTime == null || totalStudyTime == 0) {
            return "0분";
        }

        long hours = totalStudyTime / 3600;
        long minutes = (totalStudyTime % 3600) / 60;

        if (hours == 0) {
            return minutes + "분";
        } else if (minutes == 0) {
            return hours + "시간";
        } else {
            return hours + "시간 " + minutes + "분";
        }
    }

    @Schema(description = "시간 표현 (HH:MM)", example = "01:30")
    public String getFormattedTime() {
        if (totalStudyTime == null) {
            return "00:00";
        }

        long hours = totalStudyTime / 3600;
        long minutes = (totalStudyTime % 3600) / 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}