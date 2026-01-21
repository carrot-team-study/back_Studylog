package com.studylog.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberResponse { //우리서버에서 나가는 데이터

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
    private String accessToken;
    private String refreshToken;
    }
}
