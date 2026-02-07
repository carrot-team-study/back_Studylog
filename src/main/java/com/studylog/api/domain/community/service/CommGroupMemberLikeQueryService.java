package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.GroupMemberLikeDto;

import java.util.List;

public interface CommGroupMemberLikeQueryService {
    List<GroupMemberLikeDto> getGroupMembersWithLikes(Long groupId, Long viewerId);
}
