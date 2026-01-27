package com.studylog.api.global.auth;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import com.studylog.api.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentMemberProvider {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public Long getMemberId(String authorizationHeader){
        //헤더가 아예 없거나 프론트가 토큰을 다르게 보냈거나 토큰이 아닌 값일 때
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        //앞에 "Bearer "를 잘라서 토큰만 남긴다.
        String token = authorizationHeader.substring(7).trim();
        if (token.isEmpty()) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        // JWT 모양(점 2개) 간단 체크: JWT 아닌 값이 들어오는 걸 1차로 컷
        long dotCount = token.chars().filter(c -> c == '.').count();
        if (dotCount != 2) {
            throw new BusinessException(ErrorCode.VALIDATION_FAIL);
        }

        //토큰 유효성 검사(서명의 위조 여부, 만료 여부, 토큰 포맷 깨짐 여부)
        if (!jwtUtil.validateToken(token)){
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        //토큰에서 이메일 추출
        String email = jwtUtil.getEmailFromToken(token);

        //이메일로 멤버 조회
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(()->new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        //멤버아이디 반환
        return member.getMemberId();
    }

}