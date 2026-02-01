package com.studylog.api.domain.reflection.service;

import com.studylog.api.domain.reflection.dto.request.ReflectionRequestDto;
import com.studylog.api.domain.reflection.dto.response.ReflectionResponseDto;
import com.studylog.api.domain.reflection.entity.Reflection;
import com.studylog.api.domain.reflection.repository.ReflectionRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;

    // 회고 작성
    public void createReflection(Long memberId, ReflectionRequestDto dto) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REFLECTION_CONTENT);
        }

        LocalDate today = LocalDate.now();
        reflectionRepository.findByMemberIdAndCreatedAt(memberId, today)
                .ifPresent(r -> {
                    throw new BusinessException(ErrorCode.REFLECTION_ALREADY_EXISTS);
                });

        Reflection reflection = Reflection.builder()
                .memberId(memberId)
                .content(dto.getContent())
                .createdAt(today)
                .build();

        reflectionRepository.save(reflection);
    }

    // 회고 수정 (본인 전용)
    public void updateReflection(Long reflectionId, ReflectionRequestDto dto, Long memberId) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REFLECTION_CONTENT);
        }

        Reflection reflection = reflectionRepository.findById(reflectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFLECTION_NOT_FOUND));

        // 본인 소유인지 체크
        if (!reflection.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN); // 본인만 수정 가능
        }

        reflection.updateContent(dto.getContent());
    }

    // 회고 삭제 (본인 전용)
    public void deleteReflection(Long reflectionId, Long memberId) {
        Reflection reflection = reflectionRepository.findById(reflectionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFLECTION_NOT_FOUND));

        // 본인 소유인지 체크
        if (!reflection.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN); // 본인만 삭제 가능
        }

        reflectionRepository.delete(reflection);
    }

    // 회고 목록 조회
    @Transactional(readOnly = true)
    public List<ReflectionResponseDto> getReflectionList(Long memberId) {
        return reflectionRepository.findAllByMemberId(memberId)
                .stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt())) // 화면용 내림차순
                .map(ReflectionResponseDto::from)
                .toList();
    }
}
