package com.studylog.api.domain.todo.repository;

import com.studylog.api.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

// JpaRepository<Entity Type, Entity PK Type> : 기본 CRUD 제공
public interface TodoRepository extends JpaRepository<Todo, Long> {
    /*
     * 회원의 모든 TodoList 조회
     * JPA는 메소드명을 파싱해서 쿼리를 생성해주기에 반드시 Entity에 실제 존재하는 필드명을 사용해야 한다
     */
    List<Todo> findAllByMemberId(Long memberId);

    // 회원의 수행일 기준 TodoList 조회
    List<Todo> findAllByMemberIdAndTargetDate(Long memberId, LocalDate targetDate);
}