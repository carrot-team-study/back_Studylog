package com.studylog.api.domain.community.controller;

import com.studylog.api.domain.community.dto.*;
import com.studylog.api.domain.community.service.CommGroupMemberLikeQueryService;
import com.studylog.api.domain.community.service.CommGroupService;
import com.studylog.api.domain.community.service.CommLikeService;
import com.studylog.api.domain.community.service.CommRankingService;
import com.studylog.api.domain.todo.dto.response.TodoResponse;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

        System.out.println("### CommGroupServiceImpl.create(ownerMemberId, req) CALLED ###");
        System.out.println("### maxUser = " + req.getMaxUser());

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

    // 그룹원 목록 + likeCount + likedByMe (기존 /members 하나로 통일)
    @GetMapping("/{groupId}/members")
    public ResponseEntity<SuccessResponse<List<GroupMemberLikeDto>>> getMembersWithLikes(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authorization
    ) {
        Long viewerId = currentMemberProvider.getMemberId(authorization);

        List<GroupMemberLikeDto> data =
                commGroupMemberLikeQueryService.getGroupMembersWithLikes(groupId, viewerId);

        return ResponseEntity.ok(
                SuccessResponse.success(SuccessCode.GROUP_MEMBER_LIST_SUCCESS, data)
        );
    }

    //그룹원 투두 조회
    @GetMapping("/{groupId}/members/{memberId}/todos")
    public ResponseEntity<SuccessResponse<List<TodoResponse>>> getMemberTodosToday(
            @PathVariable Long groupId,
            @PathVariable("memberId") Long targetMemberId,
            @RequestHeader("memberId") Long viewerId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;

        List<TodoResponse> data = commGroupService.getMemberTodo(groupId, viewerId, targetMemberId, targetDate);
        return ResponseEntity.ok(SuccessResponse.success(SuccessCode.TODO_LIST_BY_DATE_SUCCESS, data));
    }


    private final CommLikeService commLikeService;
    private final CommGroupMemberLikeQueryService commGroupMemberLikeQueryService;


    // 좋아요 토글 (fromUserId도 Authorization에서 뽑기)
    @PostMapping("/{groupId}/members/{toUserId}/like")
    public ResponseEntity<SuccessResponse<LikeToggleResult>> toggleLike(
            @PathVariable Long groupId,
            @PathVariable Long toUserId,
            @RequestHeader("Authorization") String authorization
    ) {
        Long fromUserId = currentMemberProvider.getMemberId(authorization);

        LikeToggleResult data =
                commLikeService.toggleMemberLike(groupId, fromUserId, toUserId);

        return ResponseEntity.ok(
                SuccessResponse.success(SuccessCode.LIKE_SEND_SUCCESS, data)
        );
    }

    private final CommRankingService commRankingService;

    // 그룹별 받은 좋아요 TOP N
    @GetMapping("/{groupId}/rankings/likes")
    public ResponseEntity<SuccessResponse<List<RankRowDto>>> likeRanking(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authorization,
            @RequestParam(defaultValue = "20") int limit
    ) {
        Long viewerId = currentMemberProvider.getMemberId(authorization);
        List<RankRowDto> data = commRankingService.getLikeRanking(groupId, viewerId, limit);

        // SuccessCode 이름은 너 프로젝트에 맞게 추가/수정
        return ResponseEntity.ok(SuccessResponse.success(SuccessCode.GROUP_RANKING_SUCCESS, data));
    }

    // 그룹별 오늘 완료 투두 TOP N (date 없으면 오늘)
    @GetMapping("/{groupId}/rankings/todos")
    public ResponseEntity<SuccessResponse<List<RankRowDto>>> todayTodoDoneRanking(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authorization,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "20") int limit
    ) {
        Long viewerId = currentMemberProvider.getMemberId(authorization);
        List<RankRowDto> data = commRankingService.getTodayTodoDoneRanking(groupId, viewerId, date, limit);

        return ResponseEntity.ok(SuccessResponse.success(SuccessCode.GROUP_RANKING_SUCCESS, data));
    }


}
