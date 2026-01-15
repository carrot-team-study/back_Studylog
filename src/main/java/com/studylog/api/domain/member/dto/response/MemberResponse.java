package com.studylog.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @AllArgsConstructor
    public static class Login {
    private String accessToken;
    private String refreshToken;
    }
}
