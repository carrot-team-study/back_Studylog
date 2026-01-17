package com.studylog.api.domain.community.repository;

import com.studylog.api.domain.community.entity.CommGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommGroupRepository extends JpaRepository<CommGroup,Long> {

    boolean existsByGroupName(String groupName);
}
