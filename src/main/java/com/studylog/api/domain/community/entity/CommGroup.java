package com.studylog.api.domain.community.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "owner_member_id", nullable = false)
    private Long ownerMemberId;

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
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "member_count", nullable = false)
    private Integer memberCount;

    //자동 날짜 세팅
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.memberCount == null){
            this.memberCount = 0;
        }
    }

    //비밀방 : f / 공개방 : t
    public boolean isPasswordRoom(){
        return passwordHash != null;
    }

    public void increaseMemberCount() {
        if (this.memberCount == null) this.memberCount = 0;
        this.memberCount++;
    }

    public void decreaseMemberCount() {
        if (this.memberCount == null) this.memberCount = 0;
        if (this.memberCount > 0) this.memberCount--;
    }


}
