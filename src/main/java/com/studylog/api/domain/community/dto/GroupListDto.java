package com.studylog.api.domain.community.dto;

import java.time.LocalDateTime;

public record GroupListDto(
        Long groupId,
        String groupName,
        Integer memberCount,
        LocalDateTime createdAt
) {}
