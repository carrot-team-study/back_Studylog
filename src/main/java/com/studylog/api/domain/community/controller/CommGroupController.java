package com.studylog.api.domain.community.controller;

import com.studylog.api.domain.community.dto.CommGroupCreateRequest;
import com.studylog.api.domain.community.dto.CommGroupCreateResponse;
import com.studylog.api.domain.community.dto.CommGroupDetailResponse;
import com.studylog.api.domain.community.service.CommGroupService;
import com.studylog.api.global.common.code.SuccessCode;
import com.studylog.api.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comm")
@ApiResponses
@SecurityRequirement(name = "BearerAuth")
public class CommGroupController {

    private final CommGroupService commGroupService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CommGroupCreateResponse>> create(@Valid @RequestBody CommGroupCreateRequest req){
        Long groupId = commGroupService.create(req);
        SuccessCode sc = SuccessCode.GROUP_CREATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc, new CommGroupCreateResponse(groupId)));

    }

    @GetMapping("/{groupId}")
    public ResponseEntity<SuccessResponse<CommGroupDetailResponse>> getDetail(@PathVariable Long groupId){
        CommGroupDetailResponse detail = commGroupService.getDetail(groupId);
        SuccessCode sc = SuccessCode.GROUP_CREATE_SUCCESS;

        return ResponseEntity.status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc,detail));
    }
}
