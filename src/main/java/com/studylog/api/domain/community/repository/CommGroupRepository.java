package com.studylog.api.domain.community.repository;

import com.studylog.api.domain.community.entity.CommGroup;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommGroupRepository extends JpaRepository<CommGroup,Long> {

    boolean existsByGroupName(String groupName);
    Optional<CommGroup> findByGroupIdAndDeletedAtIsNull(Long groupId);

    //같은 groupId에 대해 join이 동시에 들어오면
    //먼저 잡은 트랜잭션이 끝날 때까지 다른 트랜잭션이 대기함
    //그 상태에서 member_count 체크하면 정원 초과 버그 안 남
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from CommGroup g where g.groupId = :groupId")
    Optional<CommGroup> findByIdForUpdate(@Param("groupId") Long groupId);
}
