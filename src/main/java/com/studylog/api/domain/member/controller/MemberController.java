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
    public ResponseEntity<MemberResponse.LoginResponse> login(@RequestBody MemberRequest.LoginRequest request) {
    MemberResponse.LoginResponse response = memberService.login(request);
    return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token){
        String email = jwtUtil.getEmailFromToken(token.substring(7)); //bearer제거하고 순수 jwt만 뽑기
        memberService.logout(email);
        return ResponseEntity.ok().build();

    }
    @PostMapping("/refresh")
    public ResponseEntity<MemberResponse.LoginResponse> refresh(@RequestBody String refreshToken) {
        MemberResponse.LoginResponse response = memberService.refresh(refreshToken);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signup")
    public ResponseEntity<Void> singup(@RequestBody MemberRequest.SignupRequest request){
        memberService.signup(request);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String token, @RequestBody MemberRequest.PasswordChangeRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        memberService.changePassword(email, request);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<MemberResponse.MyInfoResponse> getMyInfo(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        MemberResponse.MyInfoResponse response = memberService.getMyInfo(email);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/findemail")
    public ResponseEntity<MemberResponse.FindEmailResponse> findEmail(@RequestBody MemberRequest.FindEmailRequest request) {
        MemberResponse.FindEmailResponse response = memberService.findEmail(request);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/profile-photo")
    public ResponseEntity<Void> updateProfilePhoto(@RequestHeader("Authorization") String token, @RequestBody MemberRequest.UpdateProfilePhotoRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        memberService.updateProfilePhoto(email, request);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/profile-photo")
    public ResponseEntity<Void> deleteProfilePhoto(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        memberService.deleteProfilePhoto(email);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @RequestHeader("Authorization") String token,
            @RequestBody MemberRequest.WithdrawRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        memberService.withdraw(email, request);
        return ResponseEntity.ok().build();
    }







}
