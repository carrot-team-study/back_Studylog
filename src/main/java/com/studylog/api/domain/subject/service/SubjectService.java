package com.studylog.api.domain.subject.service;

import com.studylog.api.domain.member.entity.Member;
import com.studylog.api.domain.member.repository.MemberRepository;
import com.studylog.api.domain.plan.dto.PlanResponseDto;
import com.studylog.api.domain.subject.dto.SubjectRequestDto;
import com.studylog.api.domain.subject.dto.SubjectResponseDto;
import com.studylog.api.domain.subject.entity.Subject;
import com.studylog.api.domain.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SubjectResponseDto createSubject(Long memberId, SubjectRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다. 다시 로그인해 주세요."));

        subjectRepository.findByMemberMemberIdAndSubjectNameAndIsDeletedFalse(memberId, requestDto.getSubjectName())
                .ifPresent(subject -> {
                    throw new IllegalArgumentException("이미 존재하는 과목명입니다.");
                });

        Subject subject = requestDto.toEntity(member);
        Subject savedSubject = subjectRepository.save(subject);

        return SubjectResponseDto.from(savedSubject);
    }

    @Transactional(readOnly = true)
    public List<SubjectResponseDto> getSubjectList(Long memberId) {
        return subjectRepository.findByMemberMemberIdAndIsDeletedFalse(memberId)
                .stream()
                .map(SubjectResponseDto::from)
                .toList();
    }

    @Transactional
    public SubjectResponseDto updateSubject(Long memberId, Long subjectId, SubjectRequestDto requestDto) {
        Subject subject = subjectRepository.findBySubjectIdAndMemberMemberId(subjectId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("과목을 찾을 수 없습니다."));

        if (subject.getIsDeleted()) {
            throw new IllegalArgumentException("삭제된 과목은 수정할 수 없습니다.");
        }

        subjectRepository.findByMemberMemberIdAndSubjectNameAndIsDeletedFalse(memberId, requestDto.getSubjectName())
                .ifPresent(existingSubject -> {
                    if (!existingSubject.getSubjectId().equals(subjectId)) {
                        throw new IllegalArgumentException("이미 존재하는 과목명입니다.");
                    }
                });

        subject.updateSubject(requestDto.getSubjectName());

        return SubjectResponseDto.from(subject);
    }

    @Transactional
    public void deleteSubject(Long memberId, Long subjectId) {
        Subject subject = subjectRepository.findBySubjectIdAndMemberMemberId(subjectId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("과목을 찾을 수 없습니다."));

        if (subject.getIsDeleted()) {
            throw new IllegalArgumentException("이미 삭제된 과목입니다.");
        }

        subject.delete();
    }
}
