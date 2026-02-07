package com.studylog.api.domain.community.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CommGroupCreateRequest {


    @NotBlank
    private String groupName;

    private String groupIntro;
    private String password; // 원문 비번(받아서 해시로 저장한다고 가정)
    private String passwordConfirm;//비밀번호 확인

    @NotNull
    @Min(1)
    @Max(50)
    private Long maxUser;
    private Long dailyGoal;
    private List<Long> tagIds;

}
