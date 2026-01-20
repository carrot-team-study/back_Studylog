package com.studylog.api.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @NoArgsConstructor
    public static class LoginRequest{
    private String email;
    private String password;
    }
    @Getter
    @NoArgsConstructor
    public static class SignupRequest{
        private String email;
        private String password;
        private String name;
        private String nickname;
    }
}
