package com.studylog.api.domain.member.entity;

import com.studylog.api.domain.member.dto.response.KakaoUserResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_email", unique = true, nullable = true)
    private String memberEmail;

    @Column(name = "member_password", nullable = true)
    private String memberPassword;

    @Column(name = "member_nickname")
    private String memberNickname;

    @Column(name = "member_profile_photo", nullable = true)
    private String memberProfilePhoto;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Column(name = "member_type", nullable = false)
    private String memberType;

    @Column(name = "member_status")
    private String memberStatus;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static Member fromKakao(KakaoUserResponse user) {
        String socialId = String.valueOf(user.getId());

        String email = (user.getKakaoAccount() != null) ? user.getKakaoAccount().getEmail() : null;

        // ✅ email 없으면 대체값 생성
        if (email == null || email.isBlank()) {
            email = "kakao_" + socialId + "@social.local";
        }

        return Member.builder()
                .memberEmail(email)
                .memberName(user.getKakaoAccount() != null && user.getKakaoAccount().getProfile() != null
                        ? user.getKakaoAccount().getProfile().getNickname()
                        : "KAKAO_USER")
                .memberNickname(user.getKakaoAccount() != null && user.getKakaoAccount().getProfile() != null
                        ? user.getKakaoAccount().getProfile().getNickname()
                        : "KAKAO_USER")
                .memberProfilePhoto(user.getKakaoAccount() != null && user.getKakaoAccount().getProfile() != null
                        ? user.getKakaoAccount().getProfile().getProfileImageUrl()
                        : null)
                .socialId(socialId)
                .memberType("KAKAO")
                .memberStatus("ACTIVE")
                .memberPassword("SOCIAL_LOGIN") // 또는 임시값
                .build();

    }
}
