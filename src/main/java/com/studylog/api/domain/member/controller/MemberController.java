package com.studylog.api.domain.member.controller;


import com.studylog.api.domain.member.dto.request.MemberRequest;
import com.studylog.api.domain.member.dto.response.MemberResponse;
import com.studylog.api.domain.member.service.MemberService;
import com.studylog.api.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<MemberResponse.Login> login(@RequestBody MemberRequest.Login request) {
    MemberResponse.Login response = memberService.login(request);
    return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token){
        String email = jwtUtil.getEmailFromToken(token.substring(7)); //bearer제거하고 순수 jwt만 뽑기
        memberService.logout(email);
        return ResponseEntity.ok().build();

    }
    @PostMapping("/refresh")
    public ResponseEntity<MemberResponse.Login> refresh(@RequestBody String refreshToken) {
        MemberResponse.Login response = memberService.refresh(refreshToken);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signup")
    public ResponseEntity<Void> singup(@RequestBody MemberRequest.Singup request){
        memberService.signup(request);
        return ResponseEntity.ok().build();
    }

}
