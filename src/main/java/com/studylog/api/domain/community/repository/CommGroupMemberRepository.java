package com.studylog.api.domain.community.repository;

import com.studylog.api.domain.community.dto.GroupListDto;
import com.studylog.api.domain.community.dto.MemberListDto;
import com.studylog.api.domain.community.entity.CommGroupMember;
import com.studylog.api.domain.community.entity.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommGroupMemberRepository extends JpaRepository<CommGroupMember, Long> {
    Optional<CommGroupMember> findByGroup_GroupIdAndMember_MemberId(Long groupGroupId, Long memberMemberId);
    boolean existsByGroup_GroupIdAndMember_MemberIdAndMemberStatus(Long groupId, Long memberId, MemberStatus memberStatus);

    @Query("""
    select new com.studylog.api.domain.community.dto.MemberListDto(
        m.memberId,
        m.memberEmail,
        m.memberNickname,
        gm.role,
        gm.memberStatus,
        gm.joinedAt
    )
        from CommGroupMember gm
        join gm.member m
        where gm.group.groupId = :groupId
          and gm.memberStatus = :status
        order by
          case when gm.role = 'OWNER' then 0 else 1 end,
          gm.joinedAt asc
    """)
    List<MemberListDto> findMembersByGroupId(@Param("groupId") Long groupId, @Param("status") MemberStatus status);

    @Query(value = """
    select
        m.member_id as memberId,
        m.member_nickname as nickname,
        coalesce(cnt.like_cnt, 0) as likeCount,
        case when me.like_id is null then false else true end as likedByMe
    from group_member gm
    join member m on m.member_id = gm.member_id

    left join (
        select to_user_id, count(*) as like_cnt
        from comm_like
        where group_id = :groupId
        group by to_user_id
    ) cnt on cnt.to_user_id = m.member_id

    left join comm_like me
        on me.group_id = :groupId
       and me.from_user_id = :viewerId
       and me.to_user_id = m.member_id

    where gm.group_id = :groupId
      and gm.member_status = 'ACTIVE'
    order by
      case when gm.role = 'OWNER' then 0 else 1 end,
      gm.joined_at asc
""", nativeQuery = true)
    List<GroupMemberLikeView> findMembersWithLikes(@Param("groupId") Long groupId,
                                                   @Param("viewerId") Long viewerId);



    //그룹별 받은 좋아요
    @Query(value = """
    select
      m.member_id as memberId,
      m.member_nickname as nickname,
      coalesce(x.cnt, 0) as score
    from group_member gm
    join member m on m.member_id = gm.member_id
    left join (
      select to_user_id, count(*) as cnt
      from comm_like
      where group_id = :groupId
      group by to_user_id
    ) x on x.to_user_id = m.member_id
    where gm.group_id = :groupId
      and gm.member_status = 'ACTIVE'
    order by score desc,
             case when gm.role = 'OWNER' then 0 else 1 end,
             gm.joined_at asc
    limit :limit
""", nativeQuery = true)
    List<RankRowView> findLikeRanking(@Param("groupId") Long groupId, @Param("limit") int limit);


    //그룹별 오늘 완료 투두
    @Query(value = """
    select
      m.member_id as memberId,
      m.member_nickname as nickname,
      count(*) as score
    from todo t
    join group_member gm
      on gm.member_id = t.member_id
     and gm.group_id = :groupId
     and gm.member_status = 'ACTIVE'
    join member m on m.member_id = t.member_id
    where t.target_date = :date
      and t.is_completed = true
    group by m.member_id, m.member_nickname
    order by score desc
    limit :limit
""", nativeQuery = true)
    List<RankRowView> findTodayDoneTodoRanking(@Param("groupId") Long groupId,
                                               @Param("date") LocalDate date,
                                               @Param("limit") int limit);


}

