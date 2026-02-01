package com.studylog.api.domain.community.repository;

import com.studylog.api.domain.community.entity.CommGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommGroupMemberRepository extends JpaRepository<CommGroupMember, Long> {
    Optional<CommGroupMember> findByGroup_GroupIdAndMember_MemberId(Long groupGroupId, Long memberMemberId);
}
