package com.studylog.api.domain.plan.repository;

import com.studylog.api.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan,Long> {

}
