package com.studylog.api.domain.community.repository;

import com.studylog.api.domain.community.entity.CommTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommTagRepository extends JpaRepository<CommTag, Long> {
//태그 테이블 조회/저장 하는 곳

    Optional<CommTag> findByCode(String code);//코드로 태그 찾기
    boolean existsByCode(String code);//코드 중복체크
    List<CommTag> findAllByIsActiveTrueOrderBySortOrderAsc();//태그 목록
}
