package com.studylog.api.domain.subject.dto;

import com.studylog.api.domain.subject.entity.Subject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "SubjectResponseDto", description = "과목 정보 응답 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SubjectResponseDto {

    @Schema(description = "과목 ID")
    private Long subjectId;

    @Schema(description = "회원 ID")
    private Long memberId;

    @Schema(description = "과목명")
    private String subjectName;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 여부")
    private Boolean isDeleted;

    public static SubjectResponseDto from(Subject subject) {
        return SubjectResponseDto.builder()
                .subjectId(subject.getSubjectId())
                .memberId(subject.getMember().getMemberId())
                .subjectName(subject.getSubjectName())
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .isDeleted(subject.getIsDeleted())
                .build();
    }
}
