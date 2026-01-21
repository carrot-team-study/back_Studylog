package com.studylog.api.domain.member.service;

import com.studylog.api.domain.member.client.KakaoOAuthClient;
import com.studylog.api.domain.member.dto.response.KakaoTokenResponse;
import com.studylog.api.domain.member.dto.response.KakaoUserResponse;
import com.studylog.api.domain.member.dto.response.MemberResponse;
import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class KakaoService {


    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public MemberResponse.LoginResponse loginWithKakao(String code) {

        KakaoTokenResponse token = kakaoOAuthClient.fetchToken(code);
        KakaoUserResponse user = kakaoOAuthClient.fetchUser(token.getAccessToken());

        String socialId = String.valueOf(user.getId());
        String email = (user.getKakaoAccount() != null) ? user.getKakaoAccount().getEmail() : null;

        // ✅ email이 없으면 socialId 기반으로 회원 조회/가입
        Member member = (email != null && !email.isBlank())
                ? memberRepository.findByMemberEmail(email)
                .orElseGet(() -> memberRepository.save(Member.fromKakao(user)))
                : memberRepository.findBySocialIdAndMemberType(socialId, "KAKAO")
                .orElseGet(() -> memberRepository.save(Member.fromKakao(user)));

        // JWT 발급 기준: email이 없으면 socialId로 토큰 subject를 잡아야 함
        String subject = (email != null && !email.isBlank()) ? email : "KAKAO:" + socialId;

        String accessToken = jwtUtil.generateAccessToken(subject);
        String refreshToken = jwtUtil.generateRefreshToken(subject);
        refreshTokenService.saveRefreshToken(subject, refreshToken);

        return new MemberResponse.LoginResponse(accessToken, refreshToken);
    }
}
