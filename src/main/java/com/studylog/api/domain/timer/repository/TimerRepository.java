package com.studylog.api.domain.timer.repository;

import com.studylog.api.domain.timer.entity.Timer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TimerRepository extends JpaRepository<Timer, Long> {
    List<Timer> findAllByMemberIdAndTimerDate(Long memberId, LocalDate timerDate);
}