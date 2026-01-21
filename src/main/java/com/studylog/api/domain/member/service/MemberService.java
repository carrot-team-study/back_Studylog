package com.studylog.api.domain.member.service;

import com.studylog.api.domain.member.dto.request.MemberRequest;
import com.studylog.api.domain.member.dto.response.MemberResponse;
import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final JwtUtil jwtUtil; //토큰 생성
    private final RefreshTokenService refreshTokenService; //refreshtoekn 저장
    private final PasswordEncoder passwordEncoder; // 비밀번호 검증
    private final MemberRepository memberRepository;

    public MemberResponse.LoginResponse login(MemberRequest.LoginRequest request){
        String email = request.getEmail();
        String password = request.getPassword();

        // 1. DB에서 사용자 조회
        Member member = memberRepository.findByMemberEmail(email)
             .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 2. 비밀번호 검증
         if (!passwordEncoder.matches(password, member.getMemberPassword()) ) {
             throw new RuntimeException("비밀번호가 일치하지 않습니다");
         }

        // 3. AccessToken 생성
        String accessToken = jwtUtil.generateAccessToken(email);

        // 4. RefreshToken 생성
        String refreshToken = jwtUtil.generateRefreshToken(email);

        // 5. RefreshToken을 Redis에 저장
        refreshTokenService.saveRefreshToken(email, refreshToken);

        // 6. 토큰 반환
        return new MemberResponse.LoginResponse(accessToken, refreshToken);

    }
    public void logout(String email){
        refreshTokenService.deleteRefreshToken(email);
        SecurityContextHolder.clearContext();
    }
    public MemberResponse.LoginResponse refresh(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 RefreshToken");
        }

        String email = jwtUtil.getEmailFromToken(refreshToken);
        String savedToken = refreshTokenService.getRefreshToken(email);

        if (!refreshToken.equals(savedToken)) {
            throw new RuntimeException("RefreshToken이 일치하지 않습니다");
        }

        String newAccessToken = jwtUtil.generateAccessToken(email);
        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        refreshTokenService.saveRefreshToken(email, newRefreshToken);

        return new MemberResponse.LoginResponse(newAccessToken, newRefreshToken);
    }
    @Transactional
    public void signup(MemberRequest.SignupRequest request) {
        if (memberRepository.existsByMemberEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다");
        }
        Member member = Member.builder()
                .memberEmail(request.getEmail())
                .memberPassword(passwordEncoder.encode(request.getPassword()))
                .memberName(request.getName())
                .memberNickname(request.getNickname())
                .memberProfilePhoto("DEFAULT")
                .memberType("GENERAL")
                .memberStatus("ACTIVE")
                .build();

        memberRepository.save(member);


    }
    }
