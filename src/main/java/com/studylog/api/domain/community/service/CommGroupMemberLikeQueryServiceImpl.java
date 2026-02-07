package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.GroupMemberLikeDto;
import com.studylog.api.domain.community.repository.CommGroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommGroupMemberLikeQueryServiceImpl implements CommGroupMemberLikeQueryService{

    private final CommGroupMemberRepository commGroupMemberRepository;

    @Override
    public List<GroupMemberLikeDto> getGroupMembersWithLikes(Long groupId, Long viewerId) {
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
