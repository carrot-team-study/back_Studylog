package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.RankRowDto;
import com.studylog.api.domain.community.entity.MemberStatus;
import com.studylog.api.domain.community.repository.CommGroupMemberRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommRankingServiceImpl implements CommRankingService{

    private final CommGroupMemberRepository commGroupMemberRepository;

    //Limit 제한(몇 명까지 보여줄지) 기본 20, 최대 50
    private int clampLimit(int limit) {
        if (limit <= 0) return 20;
        return Math.min(limit, 50);
    }

    private void assertViewerActiveMember(Long groupId, Long viewerId) {
        boolean ok = commGroupMemberRepository
                .existsByGroup_GroupIdAndMember_MemberIdAndMemberStatus(groupId, viewerId, MemberStatus.ACTIVE);
        if (!ok) throw new BusinessException(ErrorCode.FORBIDDEN);
    }


    @Override
    public List<RankRowDto> getLikeRanking(Long groupId, Long viewerId, int limit) {
        assertViewerActiveMember(groupId, viewerId);

        int safeLimit = clampLimit(limit);

        return commGroupMemberRepository.findLikeRanking(groupId, safeLimit).stream()
                .map(v -> new RankRowDto(
                        v.getMemberId(),
                        v.getNickname(),
                        v.getScore() == null ? 0L : v.getScore()
                ))
                .toList();
    }

    @Override
    public List<RankRowDto> getTodayTodoDoneRanking(Long groupId, Long viewerId, LocalDate date, int limit) {
        assertViewerActiveMember(groupId, viewerId);

        int safeLimit = clampLimit(limit);
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;

        return commGroupMemberRepository.findTodayDoneTodoRanking(groupId, targetDate, safeLimit).stream()
                .map(v -> new RankRowDto(
                        v.getMemberId(),
                        v.getNickname(),
                        v.getScore() == null ? 0L : v.getScore()
                ))
                .toList();
    }
}
