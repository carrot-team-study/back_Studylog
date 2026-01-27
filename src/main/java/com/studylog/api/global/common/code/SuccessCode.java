package com.studylog.api.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // COMMON
    OK(HttpStatus.OK, "SUCCESS_200", "요청 성공"),
    CREATED(HttpStatus.CREATED, "SUCCESS_201", "생성 성공"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "SUCCESS_204", "처리 성공"),

    // AUTH
    LOGIN_SUCCESS(HttpStatus.OK, "AUTH_200", "로그인 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK, "AUTH_201", "로그아웃 성공"),
    TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "AUTH_202", "토큰 재발급 성공"),

    // USER
    SIGNUP_SUCCESS(HttpStatus.CREATED, "USER_201", "회원가입 성공"),
    WITHDRAW_SUCCESS(HttpStatus.NO_CONTENT, "USER_204", "회원탈퇴 성공"),
    PASSWORD_CHANGE_SUCCESS(HttpStatus.OK, "USER_200", "비밀번호 변경 성공"),
    PROFILE_IMAGE_UPLOAD_SUCCESS(HttpStatus.CREATED, "USER_205", "프로필 이미지 등록 성공"),
    PROFILE_IMAGE_UPDATE_SUCCESS(HttpStatus.OK, "USER_206", "프로필 이미지 수정 성공"),
    PROFILE_IMAGE_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "USER_207", "프로필 이미지 삭제 성공"),

    // SUBJECT
    SUBJECT_CREATE_SUCCESS(HttpStatus.CREATED, "SUBJECT_201", "과목 등록 성공"),
    SUBJECT_UPDATE_SUCCESS(HttpStatus.OK, "SUBJECT_200", "과목 수정 성공"),
    SUBJECT_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "SUBJECT_204", "과목 삭제 성공"),
    SUBJECT_LIST_SUCCESS(HttpStatus.OK, "SUBJECT_200_1", "과목 목록 조회 성공"),

    // RESOURCE
    RESOURCE_CREATE_SUCCESS(HttpStatus.CREATED, "RESOURCE_201", "리소스 등록 성공"),
    RESOURCE_UPDATE_SUCCESS(HttpStatus.OK, "RESOURCE_200", "리소스 수정 성공"),
    RESOURCE_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "RESOURCE_204", "리소스 삭제 성공"),
    RESOURCE_LIST_SUCCESS(HttpStatus.OK, "RESOURCE_200_1", "리소스 목록 조회 성공"),
    RESOURCE_DETAIL_SUCCESS(HttpStatus.OK, "RESOURCE_200_2", "리소스 상세 조회 성공"),

    // PLAN
    PLAN_CREATE_SUCCESS(HttpStatus.CREATED, "PLAN_201", "계획 등록 성공"),
    PLAN_UPDATE_SUCCESS(HttpStatus.OK, "PLAN_200", "계획 수정 성공"),
    PLAN_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "PLAN_204", "계획 삭제 성공"),
    PLAN_COMPLETE_SUCCESS(HttpStatus.OK, "PLAN_200_1", "계획 완료 처리 성공"),
    PLAN_LIST_BY_DATE_SUCCESS(HttpStatus.OK, "PLAN_200_2", "날짜별 계획 조회 성공"),

    // TIMER
    TIMER_START_SUCCESS(HttpStatus.OK, "TIMER_200", "타이머 시작 성공"),
    TIMER_END_SUCCESS(HttpStatus.OK, "TIMER_200_1", "타이머 종료 성공"),
    TIMER_AUTO_END_SUCCESS(HttpStatus.OK, "TIMER_200_2", "타이머 자동 종료 성공"),
    STUDY_LOG_LIST_SUCCESS(HttpStatus.OK, "TIMER_200_3", "학습기록 조회 성공"),
    STUDY_LOG_SUMMARY_SUCCESS(HttpStatus.OK, "TIMER_200_4", "학습기록 요약 성공"),
    TIMER_STATUS_SUCCESS(HttpStatus.OK, "TIMER_200_5", "타이머 상태 조회 성공"),
    TIMER_PAUSE_SUCCESS(HttpStatus.OK, "TIMER_200_6", "타이머 일시정지 성공"),
    TIMER_RESUME_SUCCESS(HttpStatus.OK, "TIMER_200_7", "타이머 재개 성공"),
    MANUAL_STUDY_LOG_SUCCESS(HttpStatus.CREATED, "TIMER_201", "수동 학습 기록 성공"),

    // REFLECTION
    REFLECTION_CREATE_SUCCESS(HttpStatus.CREATED, "REFLECT_201", "회고 작성 성공"),
    REFLECTION_UPDATE_SUCCESS(HttpStatus.OK, "REFLECT_200", "회고 수정 성공"),
    REFLECTION_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "REFLECT_204", "회고 삭제 성공"),
    REFLECTION_DAILY_SUCCESS(HttpStatus.OK, "REFLECT_200_1", "회고 일별 조회 성공"),
    REFLECTION_LIST_SUCCESS(HttpStatus.OK, "REFLECT_200_2", "회고 목록 조회 성공"),

    // STATS / DASHBOARD
    DASHBOARD_MAIN_SUCCESS(HttpStatus.OK, "STATS_200", "메인 대시보드 조회 성공"),
    WEEKLY_STATS_SUCCESS(HttpStatus.OK, "STATS_200_1", "주간 통계 조회 성공"),
    MONTHLY_STATS_SUCCESS(HttpStatus.OK, "STATS_200_2", "월간 통계 조회 성공"),
    ATTENDANCE_CALENDAR_SUCCESS(HttpStatus.OK, "STATS_200_3", "출석 달력 조회 성공"),

    // GROUP
    GROUP_CREATE_SUCCESS(HttpStatus.CREATED, "GROUP_201", "그룹 생성 성공"),
    GROUP_JOIN_SUCCESS(HttpStatus.OK, "GROUP_200", "그룹 가입 성공"),
    GROUP_JOIN_REQUEST_SUCCESS(HttpStatus.OK, "GROUP_200_1", "그룹 가입 요청 성공"),
    GROUP_MEMBER_LIST_SUCCESS(HttpStatus.OK, "GROUP_200_2", "그룹원 목록 조회 성공"),
    GROUP_ROLE_UPDATE_SUCCESS(HttpStatus.OK, "GROUP_200_3", "권한 변경 성공"),
    GROUP_KICK_SUCCESS(HttpStatus.NO_CONTENT, "GROUP_204", "그룹원 강퇴 성공"),
    GROUP_PLAN_LIST_SUCCESS(HttpStatus.OK, "GROUP_200_4", "그룹 내 계획 조회 성공"),

    // RANK / LIKE
    GROUP_RANKING_SUCCESS(HttpStatus.OK, "RANK_200", "그룹 랭킹 조회 성공"),
    LIKE_SEND_SUCCESS(HttpStatus.CREATED, "LIKE_201", "좋아요 전송 성공"),

    // NOTIFICATION
    NOTIFICATION_LIST_SUCCESS(HttpStatus.OK, "NOTI_200", "알림 조회 성공"),
    NOTIFICATION_READ_SUCCESS(HttpStatus.OK, "NOTI_200_1", "알림 읽음 처리 성공"),
    NOTIFICATION_SETTING_UPDATE_SUCCESS(HttpStatus.OK, "NOTI_200_2", "알림 설정 변경 성공"),

    // TODOLIST
    TODO_CREATE_SUCCESS(HttpStatus.CREATED, "TODO_201", "Todo 등록 성공"),
    TODO_UPDATE_SUCCESS(HttpStatus.OK, "TODO_200", "Todo 수정 성공"),
    TODO_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "TODO_204", "Todo 삭제 성공"),
    TODO_STATUS_UPDATE_SUCCESS(HttpStatus.OK, "TODO_200_1", "Todo 상태 변경 성공"),
    TODO_LIST_SUCCESS(HttpStatus.OK, "TODO_200_2", "Todo 목록 조회 성공"),
    TODO_LIST_BY_DATE_SUCCESS(HttpStatus.OK, "TODO_200_3", "날짜별 Todo 목록 조회 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
