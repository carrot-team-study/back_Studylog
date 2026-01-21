package com.studylog.api.domain.member.client;

import com.studylog.api.domain.member.dto.response.KakaoTokenResponse;
import com.studylog.api.domain.member.dto.response.KakaoUserResponse;
import com.studylog.api.global.config.KakaoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * 카카오 서버와 통신하는 Client 레이어입니다.
 * - Service는 비즈니스 흐름(회원 매칭/토큰 발급)에 집중
 * - Client는 "외부 API 호출" 책임만 갖도록 분리합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    private final WebClient.Builder webClientBuilder;
    private final KakaoProperties kakaoProperties;

    private static final String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com";
    private static final String KAKAO_API_BASE_URL = "https://kapi.kakao.com";

    /**
     * 인가 코드로 카카오 토큰을 발급받습니다.
     */
    public KakaoTokenResponse fetchToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", kakaoProperties.getClientId());
        form.add("redirect_uri", kakaoProperties.getRedirectUri());
        form.add("code", code);

        try {
            return webClientBuilder.baseUrl(KAKAO_AUTH_BASE_URL)
                    .build()
                    .post()
                    .uri("/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(form)
                    .retrieve()
                    .bodyToMono(KakaoTokenResponse.class)
                    .block();
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            // ✅ 카카오가 왜 거절했는지 body에 설명이 들어있음
            log.error("Kakao token request failed. status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    /**
     * 카카오 access_token으로 사용자 정보를 조회합니다.
     */
    public KakaoUserResponse fetchUser(String kakaoAccessToken) {
        return webClientBuilder.baseUrl(KAKAO_API_BASE_URL)
                .build()
                .get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .block();
    }
}
