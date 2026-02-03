package com.studylog.api.domain.statistics.entity;

import com.studylog.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statId;

    @Column(nullable = false)
    private LocalDate statDate;

    @Column(nullable = false)
    private Long totalStudyTime;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Stat(Member member, LocalDate statDate, Long totalStudyTime) {
        this.member = member;
        this.statDate = statDate;
        this.totalStudyTime = totalStudyTime;
    }
}
