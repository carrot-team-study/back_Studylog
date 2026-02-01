package com.studylog.api.domain.reflection.repository;

import com.studylog.api.domain.reflection.entity.Reflection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReflectionRepository extends JpaRepository<Reflection, Long> {

    Optional<Reflection> findByMemberIdAndCreatedAt(Long memberId, LocalDate createdAt);

    List<Reflection> findAllByMemberId(Long memberId);
}
