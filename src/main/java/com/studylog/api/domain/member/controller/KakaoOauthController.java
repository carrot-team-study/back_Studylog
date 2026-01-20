package com.studylog.api.domain.member.controller;

import com.studylog.api.domain.member.dto.response.MemberResponse;
import com.studylog.api.domain.member.service.KakaoService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class KakaoOauthController {

    private final KakaoService kakaoService;

    @ResponseBody
    @GetMapping("/login/oauth/kakao")
    public ResponseEntity<MemberResponse.LoginResponse> kakaoLogin(@RequestParam ("code") String code) {
        MemberResponse.LoginResponse response = kakaoService.loginWithKakao(code);
        return ResponseEntity.ok(response);
    }
}
