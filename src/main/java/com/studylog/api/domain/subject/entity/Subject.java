package com.studylog.api.domain.subject.entity;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Entity
@Table(name = "subject")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subject extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    @Column(nullable = false, length = 20)
    private String subjectName;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Subject(String subjectName, Member member) {
        this.subjectName = subjectName;
        this.member = member;
        this.isDeleted = false;
    }

    public void updateSubject(String subjectName) {
        this.subjectName = subjectName;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
