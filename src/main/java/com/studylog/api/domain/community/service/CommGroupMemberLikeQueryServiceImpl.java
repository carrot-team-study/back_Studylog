package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.GroupMemberLikeDto;
import com.studylog.api.domain.community.entity.MemberStatus;
import com.studylog.api.domain.community.repository.CommGroupMemberRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommGroupMemberLikeQueryServiceImpl implements CommGroupMemberLikeQueryService{

    private final CommGroupMemberRepository commGroupMemberRepository;

    private void assertViewerActiveMember(Long groupId, Long viewerId) {
        boolean ok = commGroupMemberRepository
                .existsByGroup_GroupIdAndMember_MemberIdAndMemberStatus(groupId, viewerId, MemberStatus.ACTIVE);

        if (!ok) throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    @Override
    public List<GroupMemberLikeDto> getGroupMembersWithLikes(Long groupId, Long viewerId) {
        assertViewerActiveMember(groupId, viewerId); // 가입자만 멤버 목록 조회 가능

        return commGroupMemberRepository.findMembersWithLikes(groupId, viewerId)
                .stream()
                .map(v -> new GroupMemberLikeDto(
                        v.getMemberId(),
                        v.getNickname(),
                        v.getLikeCount() == null ? 0 : v.getLikeCount(),
                        Boolean.TRUE.equals(v.getLikedByMe())
                ))
                .toList();
    }
}
