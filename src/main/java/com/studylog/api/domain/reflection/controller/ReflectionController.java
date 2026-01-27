package com.studylog.api.domain.reflection.controller;

import com.studylog.api.domain.reflection.dto.request.ReflectionRequestDto;
import com.studylog.api.domain.reflection.dto.response.ReflectionResponseDto;
import com.studylog.api.domain.reflection.service.ReflectionService;
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

    //  전체 회고 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<List<ReflectionResponseDto>>> list(@RequestHeader("memberId") Long memberId) {
        List<ReflectionResponseDto> reflections = reflectionService.getReflectionList(memberId);
        SuccessCode sc = SuccessCode.REFLECTION_LIST_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc, reflections));
    }

    // 회고 작성
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> create(@RequestHeader("memberId") Long memberId, @RequestBody ReflectionRequestDto dto) {
        reflectionService.createReflection(memberId, dto);
        SuccessCode sc = SuccessCode.REFLECTION_CREATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 회고 수정
    @PatchMapping("/{reflectionId}")
    public ResponseEntity<SuccessResponse<Void>> update(@PathVariable Long reflectionId, @RequestBody ReflectionRequestDto dto) {
        reflectionService.updateReflection(reflectionId, dto);
        SuccessCode sc = SuccessCode.REFLECTION_UPDATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }

    // 회고 삭제
    @DeleteMapping("/{reflectionId}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long reflectionId) {
        reflectionService.deleteReflection(reflectionId);
        SuccessCode sc =  SuccessCode.REFLECTION_DELETE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus()).body(SuccessResponse.success(sc));
    }
}
