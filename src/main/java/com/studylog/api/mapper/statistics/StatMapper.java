package com.studylog.api.mapper.statistics;

import com.studylog.api.domain.statistics.dto.StatDto;
import com.studylog.api.domain.statistics.dto.StatSubjectDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StatMapper {

    /**
     * 통계 목록 조회 (기간별 일일통계)
     * 주간/월간 차트용
     */
    List<StatDto> getStatsList(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 과목별 통계 조회 (기간별 과목통계)
     * timer 테이블 기반
     */
    List<StatSubjectDto> getSubjectStats(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 오늘 학습 시간 (실시간)
     * timer 테이블 기반
     */
    Long getTodayStudyTime(
            @Param("memberId") Long memberId,
            @Param("today") LocalDate today
    );
}
