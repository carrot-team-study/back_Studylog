package com.studylog.api.domain.community.repository;

import com.studylog.api.domain.community.entity.CommGroupTag;
import com.studylog.api.domain.community.entity.CommGroupTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommGroupTagRepository extends JpaRepository<CommGroupTag, CommGroupTagId> {

    List<CommGroupTag> findAllByIdGroupId(Long groupId);
    void deleteAllByIdGroupId(Long groupId);
    boolean existsByIdGroupIdAndIdTagId(Long groupId, Long tagId);

}
