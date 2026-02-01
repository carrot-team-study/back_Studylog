package com.studylog.api.domain.community.entity;

import com.studylog.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "group_member",
        uniqueConstraints = { //중복가입을 막기 위해서 유니크
                @UniqueConstraint(name = "uk_group_member_groupid_memberid",
                        columnNames = {"group_id", "member_id"})
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommGroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long groupMemberId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private CommGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "role")
    private String role;

    @Column(name = "member_status")
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus; //ACTIVE, LEFT, BANNED

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "left_at")
    private LocalDateTime leftAt;

    //상태 ACTIVE로 바꿈
    //joinedAt을 현재시간으로
    //leftAt은 null로
    public void activate() {
        this.memberStatus = MemberStatus.valueOf("ACTIVE");
        this.joinedAt = LocalDateTime.now();
        this.leftAt = null;
    }

    public void leave() {
        this.memberStatus = MemberStatus.valueOf("LEFT");
        this.leftAt = LocalDateTime.now();
    }
}
