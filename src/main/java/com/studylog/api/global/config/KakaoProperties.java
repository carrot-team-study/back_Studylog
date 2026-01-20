package com.studylog.api.global.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * 카카오 OAuth 설정값 바인딩 클래스
 * - application.yml(application-local.yml)의 kakao.* 값을 객체로 매핑합니다.
 * - @Value를 여러 군데 흩뿌리지 않고 한 곳에서 관리하기 위함입니다.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
    private Key key = new Key();
    private String redirectUri;

    @Getter
    @Setter
    public static class Key {
        /** 카카오 REST API 키 (client_id) */
        private String clientId;
    }

    public String getClientId() {
        return key.clientId;
    }


}
