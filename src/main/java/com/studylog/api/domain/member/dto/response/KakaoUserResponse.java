package com.studylog.api.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserResponse {
    //“카카오 서버가 보내주는 유저 정보 응답(JSON)을 파싱하기 위한 DTO 카카오 → 우리 서버로 들어오는 데이터 파싱용”

    private Long id;

    private PropertiesResponse properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    public static class PropertiesResponse {

        private String nickname;

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("thumbnail_image")
        private String thumbnailImage;
    }

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {

        private String email;

        private Profile profile;

        @JsonProperty("email_needs_agreement")
        private Boolean emailNeedsAgreement;
    }

    @Getter
    @NoArgsConstructor
    public static class Profile {

        private String nickname;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        @JsonProperty("thumbnail_image_url")
        private String thumbnailImageUrl;
    }
}
