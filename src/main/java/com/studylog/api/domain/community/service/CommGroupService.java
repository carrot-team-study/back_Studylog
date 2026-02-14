package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.*;
import com.studylog.api.domain.todo.dto.response.TodoResponse;
import com.studylog.api.domain.todo.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CommGroupService {
    Long create(Long ownerMemberId, CommGroupCreateRequest req);
    CommGroupDetailResponse getDetail(Long groupId);
    Page<GroupListDto> searchGroup(String keyword, List<Long> tagIds, CommGroupSort sort, Pageable pageable);
    void join(Long groupId, Long memberId, String password);
    void leave(Long groupId, Long memberId);
    List<MemberListDto> getGroupMembers(Long groupId);
    List<TodoResponse> getMemberTodo(Long groupId, Long viewerId, Long targetMemberId, LocalDate date);
    void deleteGroup(Long groupId, Long requesterId);
    Page<MyGroupListDto> getMyGroups(Long memberId, Pageable pageable);
}
