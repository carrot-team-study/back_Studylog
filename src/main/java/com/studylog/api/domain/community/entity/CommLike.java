package com.studylog.api.domain.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "comm_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_comm_like",
                columnNames = {"group_id", "from_user_id", "to_user_id"}
        )
)
@Getter
@NoArgsConstructor
public class CommLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_id")
    private Long likeId;

    @Column(name="group_id", nullable=false)
    private Long groupId;

    @Column(name="from_user_id", nullable=false)
    private Long fromUserId;

    @Column(name="to_user_id", nullable=false)
    private Long toUserId;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public CommLike(Long groupId, Long fromUserId, Long toUserId) {
        this.groupId = groupId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }


}
