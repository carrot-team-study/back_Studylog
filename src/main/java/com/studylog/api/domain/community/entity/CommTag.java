package com.studylog.api.domain.community.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class CommTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;          // 예: GOV, HS1 ...

    @Column(name = "name", nullable = false, length = 100)
    private String name;          // 예: 공무원, 고1 ...

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder; //목록에서 보여줄 순서

    @Column(name = "is_active", nullable = false)
    private Boolean isActive; //삭제 여부

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; //만든날짜

    @PrePersist
    public void prePersist() {//sortOrder/isActive/createdAt 값이 null이면 기본값 넣고 저장해준다.
        if (this.sortOrder == null) this.sortOrder = 0;
        if (this.isActive == null) this.isActive = true;
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
}
