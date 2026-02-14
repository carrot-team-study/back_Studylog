package com.studylog.api.domain.community.dto;

public class MeResponse {
    private final Long memberId;

    public MeResponse(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}