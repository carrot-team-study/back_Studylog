package com.studylog.api.domain.timer.repository;

import com.studylog.api.domain.timer.entity.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TimerRepository extends JpaRepository<Timer, Long> {

    /**
     * 특정 회원의 특정 날짜에 대한 타이머 기록을 과목과 함께 한 번에 조회하는 메소드
     * fetch join 사용
     */
    @Query("""
        select t
        from Timer t
        join fetch t.subject
        where t.memberId = :memberId
        and t.timerDate = :date
    """)
    List<Timer> findAllWithSubject(Long memberId, LocalDate date);

    /**
     * 특정 회원의 특정 날짜에 대한 총 학습 시간을 DB에서 계산해 반환
     * coalesce 사용
     * 해당 날짜에 기록이 없다면 null 반환하기에 0으로 반환하도록 함
     */
    @Query("""
        select coalesce(sum(t.duration), 0)
        from Timer t
        where t.memberId = :memberId
        and t.timerDate = :date
    """)
    Long sumDurationByMemberIdAndDate(Long memberId, LocalDate date);

    // 특정 회원의 특정 날짜에 대한 타이머 기록 조회
    List<Timer> findAllByMemberIdAndTimerDate(Long memberId, LocalDate timerDate);

    // 특정 회원의 특정 날짜에 대한 해당 과목의 타이머 기록 조회
    List<Timer> findAllByMemberIdAndTimerDateAndSubjectId(Long memberId, LocalDate timerDate, Long subjectId);
}