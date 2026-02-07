package com.studylog.api.domain.community.repository;

import com.studylog.api.domain.community.entity.CommLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommLikeRepository extends JpaRepository<CommLike, Long> {

    // 받은 좋아요 수
    @Query("""
        select count(cl)
        from CommLike cl
        where cl.groupId = :groupId
          and cl.toUserId = :toUserId
    """)
    long countLikes(@Param("groupId") Long groupId,
                    @Param("toUserId") Long toUserId);

    // 좋아요 취소(삭제) -> 삭제된 row 수 리턴(0 또는 1)
    @Modifying
    @Query("""
        delete
        from CommLike cl
        where cl.groupId = :groupId
          and cl.fromUserId = :fromUserId
          and cl.toUserId = :toUserId
    """)
    int deleteLike(@Param("groupId") Long groupId,
                   @Param("fromUserId") Long fromUserId,
                   @Param("toUserId") Long toUserId);

    // 내가 눌렀는지(존재 여부) - 목록에서 likedByMe 필요할 때도 씀
    @Query("""
        select case when count(cl) > 0 then true else false end
        from CommLike cl
        where cl.groupId = :groupId
          and cl.fromUserId = :fromUserId
          and cl.toUserId = :toUserId
    """)
    boolean existsLike(@Param("groupId") Long groupId,
                       @Param("fromUserId") Long fromUserId,
                       @Param("toUserId") Long toUserId);

    // 좋아요 추가(연타/중복 요청은 DB에서 무시)
    @Modifying
    @Query(value = """
        insert into comm_like(group_id, from_user_id, to_user_id)
        values (:groupId, :fromUserId, :toUserId)
        on conflict (group_id, from_user_id, to_user_id) do nothing
    """, nativeQuery = true)
    int insertIgnore(@Param("groupId") Long groupId,
                     @Param("fromUserId") Long fromUserId,
                     @Param("toUserId") Long toUserId);
}

