package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.CommGroupCreateRequest;

public interface CommGroupService {
    Long create(CommGroupCreateRequest req);
}
