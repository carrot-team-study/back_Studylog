package com.studylog.api.domain.statistics.repository;

import com.studylog.api.domain.statistics.entity.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
    // 특정 회원의 특정 날짜 통계 조회
    Optional<Stat> findByMemberMemberIdAndStatDate(Long memberId, LocalDate statDate);
}
