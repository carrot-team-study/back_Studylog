package com.studylog.api.domain.reflection.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reflection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reflection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reflectionId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    private LocalDate createdAt;

    private LocalDate updatedAt;

    @Builder
    public Reflection(Long memberId, String content, LocalDate createdAt) {
        this.memberId = memberId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDate.now();
    }
}
