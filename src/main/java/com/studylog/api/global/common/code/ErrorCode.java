package com.studylog.api.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //공통 에러(COMMON)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_000", "서버 내부 오류"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_001", "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_002", "지원하지 않는 메서드입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "COMMON_003", "지원하지 않는 Content-Type입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_004", "존재하지 않는 API입니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "COMMON_005", "요청 값 검증에 실패했습니다."),
    EMPTY_BODY(HttpStatus.BAD_REQUEST, "COMMON_006", "요청 본문이 비어있습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON_007", "요청 파라미터가 누락되었습니다."),

    // AUTH
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_003", "만료된 토큰입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_004", "권한이 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_005", "로그인 정보가 올바르지 않습니다."),
    LOGIN_TRY_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "AUTH_006", "로그인 시도 횟수를 초과했습니다."),
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST, "AUTH_007", "로그아웃 처리할 수 없습니다."),

    // USER
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER_001", "이미 사용 중인 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_002", "사용자를 찾을 수 없습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "USER_003", "비밀번호가 올바르지 않습니다."),
    INVALID_NEW_PASSWORD(HttpStatus.BAD_REQUEST, "USER_004", "새 비밀번호가 기준에 맞지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "USER_005", "새 비밀번호가 기존과 같습니다."),
    WITHDRAWN_USER(HttpStatus.FORBIDDEN, "USER_006", "탈퇴한 사용자입니다."),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_007", "프로필 이미지가 없습니다."),
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "USER_008", "파일 크기가 너무 큽니다."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "USER_009", "지원하지 않는 파일 형식입니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "USER_010", "인증 코드가 올바르지 않습니다."),
    EXPIRED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "USER_011", "인증 코드가 만료되었습니다."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "USER_012", "요청이 너무 많습니다."),

    // KAKAO
    KAKAO_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "KAKAO_001", "카카오 인증에 실패했습니다."),
    INVALID_KAKAO_AUTH_CODE(HttpStatus.BAD_REQUEST, "KAKAO_002", "카카오 인가 코드가 유효하지 않습니다."),
    KAKAO_BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "KAKAO_003", "카카오 서버 응답이 올바르지 않습니다."),
    KAKAO_EMAIL_CONFLICT(HttpStatus.CONFLICT, "KAKAO_004", "이미 다른 방식으로 가입된 이메일입니다."),

    // SUBJECT
    SUBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "SUBJECT_001", "과목을 찾을 수 없습니다."),
    DUPLICATE_SUBJECT_NAME(HttpStatus.CONFLICT, "SUBJECT_002", "이미 존재하는 과목명입니다."),
    INVALID_SUBJECT_COLOR(HttpStatus.BAD_REQUEST, "SUBJECT_003", "과목 색상 값이 올바르지 않습니다."),
    SUBJECT_DELETE_NOT_ALLOWED(HttpStatus.CONFLICT, "SUBJECT_004", "과목을 삭제할 수 없습니다."),
    SUBJECT_FORBIDDEN(HttpStatus.FORBIDDEN, "SUBJECT_005", "과목 접근 권한이 없습니다."),

    // RESOURCE
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_001", "리소스를 찾을 수 없습니다."),
    INVALID_UNIT_COUNT(HttpStatus.BAD_REQUEST, "RESOURCE_002", "단원 수가 올바르지 않습니다."),
    INVALID_PROGRESS_VALUE(HttpStatus.BAD_REQUEST, "RESOURCE_003", "진도 값이 올바르지 않습니다."),
    RESOURCE_FORBIDDEN(HttpStatus.FORBIDDEN, "RESOURCE_004", "리소스 접근 권한이 없습니다."),
    RESOURCE_DELETE_NOT_ALLOWED(HttpStatus.CONFLICT, "RESOURCE_005", "리소스를 삭제할 수 없습니다."),

    // PLAN
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN_001", "계획을 찾을 수 없습니다."),
    INVALID_TARGET_DATE(HttpStatus.BAD_REQUEST, "PLAN_002", "목표 날짜가 올바르지 않습니다."),
    INVALID_PRIORITY(HttpStatus.BAD_REQUEST, "PLAN_003", "우선순위 값이 올바르지 않습니다."),
    PLAN_ALREADY_DONE(HttpStatus.CONFLICT, "PLAN_004", "이미 완료 처리된 계획입니다."),
    PLAN_FORBIDDEN(HttpStatus.FORBIDDEN, "PLAN_005", "계획 접근 권한이 없습니다."),
    PLAN_VISIBILITY_NOT_ALLOWED(HttpStatus.CONFLICT, "PLAN_006", "공개 설정이 허용되지 않습니다."),

    // TIMER
    TIMER_ALREADY_RUNNING(HttpStatus.CONFLICT, "TIMER_001", "이미 실행 중인 타이머가 있습니다."),
    TIMER_NOT_RUNNING(HttpStatus.NOT_FOUND, "TIMER_002", "실행 중인 타이머가 없습니다."),
    INVALID_TIMER_TIME(HttpStatus.BAD_REQUEST, "TIMER_003", "타이머 시간 값이 올바르지 않습니다."),
    TIMER_ALREADY_ENDED(HttpStatus.CONFLICT, "TIMER_004", "이미 종료 처리된 타이머입니다."),
    INVALID_MANUAL_TIME(HttpStatus.BAD_REQUEST, "TIMER_005", "수동 기록 시간이 올바르지 않습니다."),
    TIMER_RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "TIMER_006", "너무 자주 호출했습니다."),
    TIMER_CONCURRENCY_CONFLICT(HttpStatus.CONFLICT, "TIMER_007", "동시 수정 충돌이 발생했습니다."),
    TIMER_MAX_TIME_EXCEEDED(HttpStatus.BAD_REQUEST, "TIMER_008", "최대 학습시간 제한을 초과했습니다."),

    // REFLECTION
    REFLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "REFLECT_001", "회고를 찾을 수 없습니다."),
    REFLECTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "REFLECT_002", "해당 날짜의 회고가 이미 존재합니다."),
    REFLECTION_FORBIDDEN(HttpStatus.FORBIDDEN, "REFLECT_003", "회고 접근 권한이 없습니다."),
    INVALID_REFLECTION_CONTENT(HttpStatus.BAD_REQUEST, "REFLECT_004", "회고 내용이 올바르지 않습니다."),

    // STATS
    INVALID_STATS_RANGE(HttpStatus.BAD_REQUEST, "STATS_001", "조회 기간이 올바르지 않습니다."),
    STATS_RANGE_TOO_LARGE(HttpStatus.BAD_REQUEST, "STATS_002", "조회 범위가 너무 큽니다."),
    STATS_CALCULATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "STATS_003", "통계 계산 중 오류가 발생했습니다."),

    // GROUP
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP_001", "그룹을 찾을 수 없습니다."),
    GROUP_FULL(HttpStatus.CONFLICT, "GROUP_002", "그룹 정원이 가득 찼습니다."),
    NOT_GROUP_MEMBER(HttpStatus.FORBIDDEN, "GROUP_003", "그룹 멤버가 아닙니다."),
    GROUP_ADMIN_REQUIRED(HttpStatus.FORBIDDEN, "GROUP_004", "그룹 관리자 권한이 필요합니다."),
    ALREADY_GROUP_MEMBER(HttpStatus.CONFLICT, "GROUP_005", "이미 가입된 그룹입니다."),
    GROUP_JOIN_PENDING(HttpStatus.CONFLICT, "GROUP_006", "가입 승인 대기 중입니다."),
    INVALID_GROUP_PASSWORD(HttpStatus.BAD_REQUEST, "GROUP_007", "그룹 비밀번호가 올바르지 않습니다."),
    CANNOT_KICK_TARGET(HttpStatus.CONFLICT, "GROUP_008", "강퇴할 수 없는 대상입니다."),
    INVALID_GROUP_SETTING(HttpStatus.BAD_REQUEST, "GROUP_009", "그룹 설정 값이 올바르지 않습니다."),
    INVITE_LINK_EXPIRED(HttpStatus.CONFLICT, "GROUP_010", "초대 링크가 만료되었습니다."),
    GROUP_NAME_DUPLICATED(HttpStatus.CONFLICT,"GROUP_011","중복돤 그룹명입니다."),
    GROUP_PASSWORD_MISMATCH(HttpStatus.CONFLICT,"GROUP_012","비말번호와 비밀번호확인이 일치하지 않습니다."),

    // LIKE / RANK
    LIKE_ALREADY_SENT(HttpStatus.CONFLICT, "LIKE_001", "이미 좋아요를 보냈습니다."),
    LIKE_SELF_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "LIKE_002", "자기 자신에게 좋아요를 보낼 수 없습니다."),
    LIKE_GROUP_MEMBER_ONLY(HttpStatus.FORBIDDEN, "LIKE_003", "그룹 멤버만 사용할 수 있습니다."),
    INVALID_RANK_FILTER(HttpStatus.BAD_REQUEST, "RANK_001", "랭킹 필터 값이 올바르지 않습니다."),
    RANK_CALCULATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RANK_002", "랭킹 계산 중 오류가 발생했습니다."),

    // NOTIFICATION
    NOTIFICATION_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NOTI_001", "알림 발송에 실패했습니다."),
    INVALID_NOTIFICATION_SETTING(HttpStatus.BAD_REQUEST, "NOTI_002", "알림 설정 값이 올바르지 않습니다."),
    NOTIFICATION_TARGET_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTI_003", "알림 대상을 찾을 수 없습니다."),

    // INFRA / REDIS / RATE LIMIT / LOCK
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "INFRA_001", "일시적으로 서비스를 사용할 수 없습니다."),
    REDIS_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "REDIS_001", "캐시/세션 서버 오류입니다."),
    RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "RATE_001", "요청이 너무 많습니다."),
    LOCK_FAIL(HttpStatus.CONFLICT, "LOCK_001", "처리 중입니다. 잠시 후 다시 시도해주세요.");






    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
