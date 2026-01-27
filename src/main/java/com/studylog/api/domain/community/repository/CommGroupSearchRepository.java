package com.studylog.api.domain.community.repository;

import com.studylog.api.domain.community.dto.CommGroupSort;
import com.studylog.api.domain.community.dto.GroupListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommGroupSearchRepository {
    Page<GroupListDto> search(String keyword, List<Long> tagIds, CommGroupSort order, Pageable pageable);
}
