package com.studylog.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberResponse { //우리서버에서 나가는 데이터

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
    private String accessToken;
    private String refreshToken;
    }
    @Getter
    @AllArgsConstructor
    public static class MyInfoResponse {
        private String email;
        private String name;
        private String nickname;
        private String profilePhoto;
        private String memberType;
        private String memberStatus;
        private LocalDateTime createdAt;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindEmailResponse {
        private String email;
    }
}
