package com.studylog.api.domain.plan.repository;

import com.studylog.api.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan,Long> {
    List<Plan> findAllByMemberIdAndTargetDateOrderByStartTimeAsc(Long memberId, LocalDate targetDate);


    @Query("SELECT COUNT(p) > 0 FROM Plan p " +
            "WHERE p.member.id = :memberId " +
            "AND p.targetDate = :targetDate " +
            "AND p.startTime < :endTime " +
            "AND p.endTime > :startTime ")
    Boolean isNewPlanOverlapped(
            @Param("memberId") Long memberId,
            @Param("targetDate") LocalDate targetDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT COUNT(p) > 0 FROM Plan p " +
            "WHERE p.member.id = :memberId " +
            "AND p.targetDate = :targetDate " +
            "AND p.startTime < :endTime " +
            "AND p.endTime > :startTime " +
            "AND p.id != :planId")
    Boolean isUpdatePlanOverlapped(
            @Param("memberId") Long memberId,
            @Param("targetDate") LocalDate targetDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("planId") Long planId
    );

}
