package com.studylog.api.domain.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class CommGroupTag {

    @EmbeddedId
    private CommGroupTagId id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

    public static CommGroupTag of(Long groupId, Long tagId) {
        return CommGroupTag.builder()
                .id(new CommGroupTagId(groupId, tagId))
                .build();
    }
}
