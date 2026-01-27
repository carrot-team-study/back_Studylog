package com.studylog.api.domain.community.controller;

import com.studylog.api.domain.community.dto.*;
import com.studylog.api.domain.community.service.CommGroupService;
import com.studylog.api.global.auth.CurrentMemberProvider;
import com.studylog.api.global.common.code.SuccessCode;
import com.studylog.api.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comm")
@ApiResponses
@SecurityRequirement(name = "BearerAuth")
public class CommGroupController {

    private final CommGroupService commGroupService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CommGroupCreateResponse>> create(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody CommGroupCreateRequest req){
        Long ownerMemberId = currentMemberProvider.getMemberId(authorization);
        Long groupId = commGroupService.create(ownerMemberId, req);
        SuccessCode sc = SuccessCode.GROUP_CREATE_SUCCESS;
        return ResponseEntity.status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc, new CommGroupCreateResponse(groupId)));

    }

    @GetMapping("/{groupId}")
    public ResponseEntity<SuccessResponse<CommGroupDetailResponse>> getDetail(@PathVariable Long groupId){
        CommGroupDetailResponse detail = commGroupService.getDetail(groupId);
        SuccessCode sc = SuccessCode.GROUP_DETAIL_SUCCESS;

        return ResponseEntity.status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc,detail));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<Page<GroupListDto>>> searchGroups(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(defaultValue = "NEW") CommGroupSort order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        //사이즈가 50보다 크면 50으로 보정
        Pageable pageable = PageRequest.of(page, Math.min(size, 50));
        Page<GroupListDto> result = commGroupService.searchGroup(keyword, tagIds, order, pageable);

        SuccessCode sc = SuccessCode.GROUP_LIST_SUCCESS;

        return ResponseEntity.status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc, result));
    }

    private final CurrentMemberProvider currentMemberProvider;

    @PostMapping("/{groupId}/join")
    public ResponseEntity<SuccessResponse<Long>> join(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authorization,
            @RequestBody(required = false) GroupJoinRequest req //ㅂ비반
    ){
        Long memberId = currentMemberProvider.getMemberId(authorization);
        String password = (req == null ? null : req.getPassword());

        commGroupService.join(groupId, memberId, password);

        SuccessCode sc = SuccessCode.GROUP_JOIN_SUCCESS;

        return ResponseEntity.status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc,memberId));
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<SuccessResponse<Long>> leave(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authorization
    ){
        Long memberId = currentMemberProvider.getMemberId(authorization);
        commGroupService.leave(groupId, memberId);

        SuccessCode sc = SuccessCode.GROUP_LEAVE_SUCCESS;

        return ResponseEntity.status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc,memberId));
    }

}
