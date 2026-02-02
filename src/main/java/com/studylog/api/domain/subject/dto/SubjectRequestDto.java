package com.studylog.api.domain.subject.dto;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.subject.entity.Subject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "SubjectRequestDto", description = "과목 정보 요청 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SubjectRequestDto {
    @NotBlank(message = "과목명은 필수입니다.")
    @Size(max = 20, message = "과목명은 20자를 초과할 수 없습니다.")
    @Schema(description = "과목명", example = "수학", requiredMode = Schema.RequiredMode.REQUIRED)
    private String subjectName;

    public Subject toEntity(Member member) {
        return Subject.builder()
                .subjectName(subjectName)
                .member(member)
                .build();
    }
}
