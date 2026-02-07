package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.RankRowDto;

import java.time.LocalDate;
import java.util.List;

public interface CommRankingService {

    //그룹별 받은 좋아요 수
    List<RankRowDto> getLikeRanking(Long groupId, Long viewerId, int limit);

    // 그룹별 오늘 완료 투두 (date 없으면 오늘)
    List<RankRowDto> getTodayTodoDoneRanking(Long groupId, Long viewerId, LocalDate date, int limit);
}
