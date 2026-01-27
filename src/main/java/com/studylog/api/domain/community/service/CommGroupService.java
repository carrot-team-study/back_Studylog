package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.CommGroupCreateRequest;
import com.studylog.api.domain.community.dto.CommGroupDetailResponse;
import com.studylog.api.domain.community.dto.CommGroupSort;
import com.studylog.api.domain.community.dto.GroupListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommGroupService {
    Long create(Long ownerMemberId, CommGroupCreateRequest req);
    CommGroupDetailResponse getDetail(Long groupId);
    Page<GroupListDto> searchGroup(String keyword, List<Long> tagIds, CommGroupSort sort, Pageable pageable);
    void join(Long groupId, Long memberId, String password);
    void leave(Long groupId, Long memberId);
}
