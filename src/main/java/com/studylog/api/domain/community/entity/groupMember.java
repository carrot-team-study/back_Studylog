package com.studylog.api.domain.community.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "group_member")
public class groupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long groupMemberId;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "role", length = 20)
    private String role;

    @Column(name = "member_status", length = 20)
    private String MemberStatus;

    @Column(name = "joined_at")
    private LocalDate joinedAt;

    @Column(name = "left_at")
    private LocalDate leftAt;
}
