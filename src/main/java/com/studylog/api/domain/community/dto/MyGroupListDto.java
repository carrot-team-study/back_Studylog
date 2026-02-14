package com.studylog.api.domain.community.dto;

import java.time.LocalDateTime;

public class MyGroupListDto {
    private final Long groupId;
    private final String groupName;
    private final String groupIntro;
    private final Integer memberCount;
    private final Long maxUser;
    private final LocalDateTime createdAt;
    private final String role;

    public MyGroupListDto(Long groupId, String groupName, String groupIntro,
                          Integer memberCount, Long maxUser,
                          LocalDateTime createdAt, String role) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.memberCount = memberCount;
        this.maxUser = maxUser;
        this.createdAt = createdAt;
        this.role = role;
    }

    public Long getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public String getGroupIntro() { return groupIntro; }
    public Integer getMemberCount() { return memberCount; }
    public Long getMaxUser() { return maxUser; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getRole() { return role; }
}