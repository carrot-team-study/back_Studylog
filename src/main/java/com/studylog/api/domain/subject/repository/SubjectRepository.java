package com.studylog.api.domain.subject.repository;

import com.studylog.api.domain.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    public List<Subject> findAllByOrderBySubjectNameAsc();
}