package com.studylog.api.domain.todo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // DB 테이블과 연결
@Table(name = "todo") // DB 테이블명 명시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 전용 생성자
public class Todo {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    // 과목 번호
    @Column(nullable = false)
    private Long subjectId;

    // 회원 번호
    @Column(nullable = false)
    private Long memberId;

    // 내용
    @Column(nullable = false)
    private String content;

    // 수행일
    private LocalDate targetDate;

    // 완료 여부
    @Column(name = "is_completed", nullable = false)
    private boolean completed;

    // 등록일
    private LocalDateTime createdAt;
    // 수정일
    private LocalDateTime updatedAt;

    @Builder
    public Todo(Long subjectId, Long memberId, String content, LocalDate targetDate) {
        this.subjectId = subjectId;
        this.memberId = memberId;
        this.content = content;
        this.targetDate = targetDate;
        this.completed = false;
    }

    // 완료 여부
    public void complete(boolean completed) {
        this.completed = completed;
    }

    public void update(String content, LocalDate targetDate) {
        this.content = content;
        this.targetDate = targetDate;
    }

    // Entity 저장/수정 시점을 생명주기로 관리
    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}