package com.studylog.api.domain.todo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // DB 테이블과 연결
@Table(name = "todo") // DB 테이블명 명시
@Getter // Entity 값을 조회하기 위해 사용 (DTO 변환 및 조회 로직용)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 전용 생성자
public class Todo {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    private Long subjectId;
    private Long memberId;

    @Column(nullable = false)
    private String content;

    private LocalDate targetDate;

    @Column(name = "is_completed")
    private boolean completed;

    // 처음 등록시 현재 시간으로 자동 저장
    @CreationTimestamp
    private LocalDateTime createdAt;

    // 수정시 현재 시간으로 자동 갱신
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    private Todo(
            Long subjectId,
            Long memberId,
            String content,
            LocalDate targetDate
    ){
        this.subjectId = subjectId;
        this.memberId = memberId;
        this.content = content;
        this.targetDate = targetDate;
        this.completed = false;
    }

    // 도메인 메서드
    public void complete(){
        this.completed = true;
    }
}
