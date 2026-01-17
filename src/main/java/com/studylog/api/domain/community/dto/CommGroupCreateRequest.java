package com.studylog.api.domain.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommGroupCreateRequest {

    @NotNull
    private Long memberId; // 로그인 붙으면 이건 빼고 Security에서 꺼내면 됨

    @NotBlank
    private String groupName;

    private String groupIntro;
    private String password; // 원문 비번(받아서 해시로 저장한다고 가정)
    private String passwordConfirm;//비밀번호 확인
    private Long maxUser;
    private Long dailyGoal;
}
