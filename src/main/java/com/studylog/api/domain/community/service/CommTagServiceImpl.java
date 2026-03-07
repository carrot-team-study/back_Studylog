package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.CommTagResponse;
import com.studylog.api.domain.community.repository.CommTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommTagServiceImpl implements CommTagService {

    private final CommTagRepository commTagRepository;

    @Override
    public List<CommTagResponse> getTags() {
        // ✅ 추천: 이름순
        return commTagRepository.findAllByIsActiveTrueOrderBySortOrderAscNameAsc()
                .stream()
                .map(CommTagResponse::from)
                .toList();

        // 만약 위 메서드 추가 안 했으면:
        // return commTagRepository.findAll().stream().map(CommTagResponse::from).toList();
    }
}
