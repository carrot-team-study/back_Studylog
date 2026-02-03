package com.studylog.api.domain.plan.repository;

import com.studylog.api.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {

    // 특정 날짜의 계획 목록 조회 (시작 시간 순 정렬)
    List<Plan> findAllByMemberMemberIdAndTargetDateOrderByStartTimeAsc(Long memberId, LocalDate targetDate);

    // 새 계획 등록 시 시간 겹치는지 확인
    @Query("SELECT COUNT(p) > 0 FROM Plan p " +
            "WHERE p.member.memberId = :memberId " +
            "AND p.targetDate = :targetDate " +
            "AND p.startTime < :endTime " +
            "AND p.endTime > :startTime ")
    Boolean isNewPlanOverlapped(
            @Param("memberId") Long memberId,
            @Param("targetDate") LocalDate targetDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    // 계획 수정 시 시간 겹치는지 확인 (본인 계획 제외)
    @Query("SELECT COUNT(p) > 0 FROM Plan p " +
            "WHERE p.member.memberId = :memberId " +
            "AND p.targetDate = :targetDate " +
            "AND p.startTime < :endTime " +
            "AND p.endTime > :startTime " +
            "AND p.planId != :planId")
    Boolean isUpdatePlanOverlapped(
            @Param("memberId") Long memberId,
            @Param("targetDate") LocalDate targetDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("planId") Long planId
    );

}
