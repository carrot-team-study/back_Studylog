package com.studylog.api.domain.reflection.controller;

import com.studylog.api.domain.reflection.dto.request.ReflectionRequestDto;
import com.studylog.api.domain.reflection.dto.response.ReflectionResponseDto;
import com.studylog.api.domain.reflection.service.ReflectionService;
import com.studylog.api.global.auth.CurrentMemberProvider;
import com.studylog.api.global.common.code.SuccessCode;
import com.studylog.api.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reflections")
public class ReflectionController {

    private final ReflectionService reflectionService;
    private final CurrentMemberProvider currentMemberProvider;

    //  전체 회고 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<List<ReflectionResponseDto>>> list(@RequestHeader("Authorization") String authorization) {
        // 로그인 회원 ID 가져오기
        Long memberId = currentMemberProvider.getMemberId(authorization);

        List<ReflectionResponseDto> reflections = reflectionService.getReflectionList(memberId);
        SuccessCode sc = SuccessCode.REFLECTION_LIST_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, reflections));
    }

    // 회고 작성
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> create(@RequestHeader("Authorization") String authorization, @RequestBody ReflectionRequestDto dto) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        reflectionService.createReflection(memberId, dto);
        SuccessCode sc = SuccessCode.REFLECTION_CREATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 회고 수정
    @PatchMapping("/{reflectionId}")
    public ResponseEntity<SuccessResponse<Void>> update(@RequestHeader("Authorization") String authorization, @PathVariable Long reflectionId, @RequestBody ReflectionRequestDto dto) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        reflectionService.updateReflection(reflectionId, dto, memberId);
        SuccessCode sc = SuccessCode.REFLECTION_UPDATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 회고 삭제
    @DeleteMapping("/{reflectionId}")
    public ResponseEntity<SuccessResponse<Void>> delete(@RequestHeader("Authorization") String authorization, @PathVariable Long reflectionId) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        reflectionService.deleteReflection(reflectionId, memberId);
        SuccessCode sc =  SuccessCode.REFLECTION_DELETE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }
}