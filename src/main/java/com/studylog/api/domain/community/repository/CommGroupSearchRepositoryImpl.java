package com.studylog.api.domain.community.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studylog.api.domain.community.dto.CommGroupSort;
import com.studylog.api.domain.community.dto.GroupListDto;
import com.studylog.api.domain.community.entity.QCommGroup;
import com.studylog.api.domain.community.entity.QCommGroupTag;
import com.studylog.api.domain.community.entity.QCommTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommGroupSearchRepositoryImpl implements CommGroupSearchRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private OrderSpecifier<?>[] orderSpecifiers(CommGroupSort sort, QCommGroup g){
        if(sort == null){
            sort = CommGroupSort.NEW;
        }

        return switch (sort){
            case NEW -> new OrderSpecifier[]{g.createdAt.desc(), g.groupId.desc()};
            case OLD -> new OrderSpecifier[]{g.createdAt.asc(), g.groupId.desc()};
            case GOAL_DESC -> new OrderSpecifier[]{g.dailyGoal.desc(), g.groupId.desc()};
            case MEMBERS_DESC -> new OrderSpecifier[]{g.memberCount.desc(),g.groupId.desc()};
        };
    }


    @Override
    public Page<GroupListDto> search(String keyword, List<Long> tagIds, CommGroupSort order, Pageable pageable) {

        QCommGroup g = QCommGroup.commGroup;
        QCommGroupTag gt = QCommGroupTag.commGroupTag;   // 중간테이블
        QCommTag t = QCommTag.commTag;

        //WHERE 조건을 상황에 따라 붙이기 위한 빌더
        BooleanBuilder where = new BooleanBuilder();

        //그룹명 검색
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.trim();
            where.and(
                g.groupName.containsIgnoreCase(kw)
                    .or(g.groupIntro.coalesce("").containsIgnoreCase(kw))
            );
        }


        // 태그 OR 필터 (tagIds 중 하나라도 포함)
        // CommGroupTag는 EmbeddedId라서 gt.id.groupId / gt.id.tagId 로 접근하는 게 포인트
        if (tagIds != null && !tagIds.isEmpty()) {
            where.and(
                JPAExpressions
                        .selectOne()
                        .from(gt)
                        .where(gt.id.groupId.eq(g.groupId)
                                .and(gt.id.tagId.in(tagIds)))
                        .exists()
            );
        }

        // 4) 목록 조회 (페이지 단위)
        List<GroupListDto> content = jpaQueryFactory
                .select(Projections.constructor(
                        GroupListDto.class,
                        g.groupId,
                        g.groupName,
                        g.memberCount,
                        g.createdAt
                ))
                .from(g)
                .where(where)
                .orderBy(orderSpecifiers(order,g))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 5) 전체 개수 (Page를 쓰면 보통 count 쿼리 1번 더 날림)
        Long total = jpaQueryFactory
                .select(g.count())
                .from(g)
                .where(where)
                .fetchOne();

        long totalCount = (total == null) ? 0 : total;

        return new PageImpl<>(content, pageable, totalCount);
    }
}
