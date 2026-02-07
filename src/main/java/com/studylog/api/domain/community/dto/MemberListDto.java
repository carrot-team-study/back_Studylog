package com.studylog.api.domain.community.dto;

import com.studylog.api.domain.community.entity.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberListDto {
    private Long memberId;
    private String memberEmail;
    private String memberNickname;
    private String role;
    private MemberStatus memberStatus;
    private LocalDateTime joinedAt;
}
