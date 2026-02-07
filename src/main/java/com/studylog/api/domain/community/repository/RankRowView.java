package com.studylog.api.domain.community.repository;

public interface RankRowView {
//Repository에서 SQL 결과를 받을 때만 쓰는 용도(내부용)
    Long getMemberId();
    String getNickname();
    Long getScore();
}
