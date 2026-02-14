package com.studylog.api.domain.community.controller;

import com.studylog.api.domain.community.dto.MeResponse;
import com.studylog.api.global.auth.CurrentMemberProvider;
import com.studylog.api.global.common.code.SuccessCode;
import com.studylog.api.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@ApiResponses
@SecurityRequirement(name = "BearerAuth")
public class MeController {

    private final CurrentMemberProvider currentMemberProvider;

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<MeResponse>> me(
            @RequestHeader("Authorization") String authorization
    ) {
        Long memberId = currentMemberProvider.getMemberId(authorization);
        // SuccessCode는 프로젝트에 맞게 하나 추가해도 되고, 일단 임시로 SUCCESS_200 써도 됨
        return ResponseEntity.ok(
                SuccessResponse.success(SuccessCode.OK, new MeResponse(memberId))
        );
    }
}