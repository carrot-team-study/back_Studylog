package com.studylog.api.domain.community.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comm_group")
public class CommGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "group_name", length = 30)
    private String groupName;

    @Column(name = "group_intro", columnDefinition = "text")
    private String groupIntro;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "max_user")
    private Long maxUser;

    @Column(name = "daily_goal")
    private Long dailyGoal;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    //자동 날짜 세팅
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDate.now();
        }
    }


}
