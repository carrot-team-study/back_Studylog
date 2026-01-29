package com.studylog.api.domain.plan.service;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.domain.plan.dto.PlanRequestDto;
import com.studylog.api.domain.plan.dto.PlanResponseDto;
import com.studylog.api.domain.plan.entity.Plan;
import com.studylog.api.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PlanResponseDto createPlan(PlanRequestDto request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다. 다시 로그인해 주세요."));

        boolean isOverlapping = planRepository.isNewPlanOverlapped(
                memberId,
                request.getTargetDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (isOverlapping) {
            throw new IllegalArgumentException("이미 계획이 있는 시간대입니다.");
        }

        Plan plan = request.toEntity(member);
        Plan savedPlan = planRepository.save(plan);

        return PlanResponseDto.from(savedPlan);
    }

    @Transactional
    public PlanResponseDto updatePlan(Long planId, PlanRequestDto request) {
        Plan plan =  planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException(planId + "번 플랜 없음"));

        boolean isOverlapping = planRepository.isUpdatePlanOverlapped(
                plan.getMember().getMemberId(),
                request.getTargetDate(),
                request.getStartTime(),
                request.getEndTime(),
                planId
        );

        if (isOverlapping) {
            throw new IllegalArgumentException("이미 계획이 있는 시간대입니다.");
        }

        plan.updateInfo(
                request.getTitle(),
                request.getContent(),
                request.getTargetDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        return PlanResponseDto.from(plan);
    }

    @Transactional
    public void deletePlan(Long planId) {
        planRepository.deleteById(planId);
    }

    @Transactional(readOnly=true)
    public PlanResponseDto getPlanById(Long planId) {
        Plan plan =  planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException(planId + "번 플랜 없음"));
        return PlanResponseDto.from(plan);
    }

    @Transactional(readOnly=true)
    public List<PlanResponseDto> getPlansByDate(Long memberId, LocalDate targetDate) {
        return planRepository.findAllByMemberIdAndTargetDateOrderByStartTimeAsc(memberId, targetDate)
                .stream()
                .map(PlanResponseDto::from)
                .toList();
    }

    @Transactional
    public PlanResponseDto togglePlanCompletion(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보 오류: 해당 계획을 찾을 수 없습니다."));

        plan.toggleCompletion();

        return PlanResponseDto.from(plan);
    }
}