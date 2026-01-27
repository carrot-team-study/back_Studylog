package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.CommGroupCreateRequest;
import com.studylog.api.domain.community.dto.CommGroupDetailResponse;
import com.studylog.api.domain.community.dto.CommGroupSort;
import com.studylog.api.domain.community.dto.GroupListDto;
import com.studylog.api.domain.community.entity.CommGroup;
import com.studylog.api.domain.community.entity.CommGroupMember;
import com.studylog.api.domain.community.entity.CommGroupTag;
import com.studylog.api.domain.community.entity.MemberStatus;
import com.studylog.api.domain.community.repository.*;
import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.studylog.api.domain.community.entity.QCommGroup.commGroup;

@Service
@RequiredArgsConstructor
public class CommGroupServiceImpl implements CommGroupService {

    private final CommGroupRepository commGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommGroupTagRepository commGroupTagRepository;
    private final CommTagRepository commTagRepository;
    private final MemberRepository memberRepository;
    private final CommGroupMemberRepository commGroupMemberRepository;

    //그룸 생성
    @Override
    @Transactional
    public Long create(Long ownerMemberId, CommGroupCreateRequest req) {


        if (commGroupRepository.existsByGroupName(req.getGroupName())){//그룹명 존재여부 확인
            throw new BusinessException(ErrorCode.GROUP_NAME_DUPLICATED);
        }


        String passwordHash = null;

        //비공개 모임이면
        if(req.getPassword() != null && !req.getPassword().isBlank()){
            if (req.getPassword().equals(req.getPasswordConfirm())){
                passwordHash = passwordEncoder.encode(req.getPassword());
            } else {
                throw new BusinessException(ErrorCode.GROUP_PASSWORD_MISMATCH);
            }
        }

        Member owner = memberRepository.findById(ownerMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (req.getMaxUser() != null && req.getMaxUser() < 1) {
            throw new BusinessException(ErrorCode.GROUP_MAX_USER_INVALID);
        }

        CommGroup group = CommGroup.builder()
                .ownerMemberId(ownerMemberId)
                .groupName(req.getGroupName())
                .groupIntro(req.getGroupIntro())
                .passwordHash(passwordHash)
                .maxUser(req.getMaxUser())
                .dailyGoal(req.getDailyGoal())
                .deletedAt(null)
                .memberCount(0)
                .build();

        CommGroup saved = commGroupRepository.save(group);

        // 그룹장 자동 가입
        CommGroupMember ownerMember = CommGroupMember.builder()
                .group(saved)
                .member(owner)
                .role("OWNER")
                .memberStatus(MemberStatus.ACTIVE)
                .build();
        ownerMember.activate();
        commGroupMemberRepository.save(ownerMember);

        //카운트 1
        saved.increaseMemberCount();

        Long groupId = saved.getGroupId();

        System.out.println("groupId = " + groupId);
        System.out.println("tagIds = " + req.getTagIds());

        //여기서 tagIds 저장(comm_group_tag insert)
        if (req.getTagIds() != null && !req.getTagIds().isEmpty()) {
            for (Long tagId : req.getTagIds()) {
                // (선택) 존재 검증: tagId가 comm_tag에 있는지 확인
                commTagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));

                commGroupTagRepository.save(CommGroupTag.of(groupId, tagId));
            }
        }

        return groupId;

    }

    //그룹 디테일
    @Override
    public CommGroupDetailResponse getDetail(Long groupId) {

        CommGroup group = commGroupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        //groupId로 연결테이블에서 태그 연결 기록 조회
        List<Long> tagIds = commGroupTagRepository.findAllByIdGroupId(groupId).stream()
                .map(gt -> gt.getId().getTagId())
                .toList();


        //응답 DTO에 tagIds까지 넣어서 리턴
        return new CommGroupDetailResponse(
                group.getGroupId(),
                group.getOwnerMemberId(),
                group.getGroupName(),
                group.getGroupIntro(),
                group.getMaxUser(),
                group.getDailyGoal(),
                group.getCreatedAt(),
                tagIds
        );
    }


    private final CommGroupSearchRepository commGroupSearchRepository;

    @Override
    public Page<GroupListDto> searchGroup(String keyword, List<Long> tagIds, CommGroupSort order, Pageable pageable) {

        // 1) size 폭주 방지
        int size = Math.min(pageable.getPageSize(), 50);

        // 2) 정렬 고정 (지금은 최신순만)
        Pageable safe = PageRequest.of(pageable.getPageNumber(), size);

        // 3) repository(QueryDSL) 호출
        return commGroupSearchRepository.search(keyword, tagIds, order, safe);

    }


    @Override
    @Transactional
    public void join(Long groupId, Long memberId, String password) {

        // 그룹 row 잠금 (동시 가입 방지)
        CommGroup commGroup = commGroupRepository.findByIdForUpdate(groupId)
                .orElseThrow(()->new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        //정원 체크
        if (commGroup.getMaxUser() != null &&
                commGroup.getMemberCount() != null &&
                commGroup.getMemberCount() >= commGroup.getMaxUser()) {
            throw new BusinessException(ErrorCode.GROUP_FULL);
        }

        //비번방
        if (commGroup.isPasswordRoom()){
            if (password == null || password.isBlank()){
                throw new BusinessException(ErrorCode.GROUP_PASSWORD_REQUIRED);
            }
            if (!passwordEncoder.matches(password, commGroup.getPasswordHash())){
                throw new BusinessException(ErrorCode.GROUP_PASSWORD_MISMATCH);
            }
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        //이미 가입했는지 확인
        CommGroupMember groupMember = commGroupMemberRepository
                .findByGroup_GroupIdAndMember_MemberId(groupId, memberId)
                .orElse(null);

        if (groupMember == null){
            CommGroupMember newMember = CommGroupMember.builder()
                    .group(commGroup)
                    .member(member)
                    .memberStatus(MemberStatus.ACTIVE)
                    .role("MEMBER")
                    .build();

            newMember.activate();
            commGroupMemberRepository.save(newMember);

            //member_count 증가
            commGroup.increaseMemberCount();
            return;
        }

        if (groupMember.getMemberStatus() == MemberStatus.BANNED){
            throw new BusinessException(ErrorCode.BANNED_GROUP_MEMBER);
        }

        if (groupMember.getMemberStatus() == MemberStatus.ACTIVE){
            throw new BusinessException(ErrorCode.ALREADY_GROUP_MEMBER);
        }

        groupMember.activate();
        commGroup.increaseMemberCount();
    }

    @Override
    @Transactional
    public void leave(Long groupId, Long memberId) {

        CommGroup commGroup = commGroupRepository.findByIdForUpdate(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));


        //가입한 적x
        CommGroupMember groupMember = commGroupMemberRepository
                .findByGroup_GroupIdAndMember_MemberId(groupId, memberId)
                .orElseThrow(()-> new BusinessException(ErrorCode.GROUP_NOT_JOIN));

        if (groupMember.getMemberStatus() != MemberStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.ALREADY_NOT_ACTIVE);
        }

        groupMember.leave();
        commGroup.decreaseMemberCount();

    }
}
