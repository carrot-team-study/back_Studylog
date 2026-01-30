package com.studylog.api.domain.subject.repository;

import com.studylog.api.domain.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {
    // 과목 목록 조회 - 회원의 삭제되지 않은 과목들
    List<Subject> findByMemberMemberIdAndIsDeletedFalse(Long memberId);

    // 과목 수정/삭제 시 권한 체크 - 과목 ID + 회원 ID로 조회
    Optional<Subject> findBySubjectIdAndMemberMemberId(Long subjectId, Long memberId);

    // 과목 등록/수정 시 중복 체크 - 같은 회원의 동일 과목명 존재 여부
    Optional<Subject> findByMemberMemberIdAndSubjectNameAndIsDeletedFalse(Long memberId, String subjectName);
}