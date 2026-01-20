package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.CommGroupCreateRequest;
import com.studylog.api.domain.community.dto.CommGroupDetailResponse;

public interface CommGroupService {
    Long create(CommGroupCreateRequest req);
    CommGroupDetailResponse getDetail(Long groupId);
}
