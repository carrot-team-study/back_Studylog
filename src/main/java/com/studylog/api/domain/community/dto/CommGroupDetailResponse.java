package com.studylog.api.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommGroupDetailResponse {

    private Long groupId;
    private Long memberId;
    private String groupName;
    private String groupIntro;
    private Long maxUser;
    private Long dailyGold;
    private LocalDateTime createdAt;
    private List<Long> tagIds;

}
