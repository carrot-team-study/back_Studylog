package com.studylog.api.domain.community.controller;

import com.studylog.api.domain.community.dto.CommGroupCreateRequest;
import com.studylog.api.domain.community.dto.CommGroupCreateResponse;
import com.studylog.api.domain.community.service.CommGroupService;
import com.studylog.api.global.common.code.SuccessCode;
import com.studylog.api.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comm")
@ApiResponses
public class CommGroupController {

    private final CommGroupService commGroupService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CommGroupCreateResponse>> create(@Valid @RequestBody CommGroupCreateRequest req){
        Long groupId = commGroupService.create(req);
        SuccessCode sc = SuccessCode.GROUP_CREATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc, new CommGroupCreateResponse(groupId)));

    }
}
