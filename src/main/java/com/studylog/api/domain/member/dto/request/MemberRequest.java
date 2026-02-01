package com.studylog.api.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest{
    private String email;
    private String password;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequest{
        private String email;
        private String password;
        private String name;
        private String nickname;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordChangeRequest{
        private String currentPassword;
        private String newPassword;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindEmailRequest{
        private String name;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfilePhotoRequest{
        private String profilePhotoUrl;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WithdrawRequest{
        private String password;
    }
}
