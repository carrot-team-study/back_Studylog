package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.CommTagResponse;

import java.util.List;

public interface CommTagService {
    List<CommTagResponse> getTags();
}
