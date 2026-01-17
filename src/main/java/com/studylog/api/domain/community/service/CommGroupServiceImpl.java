package com.studylog.api.domain.community.service;

import com.studylog.api.domain.community.dto.CommGroupCreateRequest;
import com.studylog.api.domain.community.entity.CommGroup;
import com.studylog.api.domain.community.repository.CommGroupRepository;
import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommGroupServiceImpl implements CommGroupService {

    private final CommGroupRepository commGroupRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long create(CommGroupCreateRequest req) {


        if (commGroupRepository.existsByGroupName(req.getGroupName())){//그룹명 존재여부 확인
            throw new BusinessException(ErrorCode.GROUP_NAME_DUPLICATED);
        }


        String passwordHash = null;

        //비공개 모임이면
        if(req.getPassword() != null && !req.getPassword().isBlank()){
            if (req.getPassword().equals(req.getPasswordConfirm())){
                passwordHash = passwordEncoder.encode(req.getPassword());
            } else {
                throw new BusinessException(ErrorCode.GROUP_PASSWORD_MISMATCH);
            }
        }


        CommGroup group = CommGroup.builder()
                .memberId(req.getMemberId())
                .groupName(req.getGroupName())
                .groupIntro(req.getGroupIntro())
                .passwordHash(passwordHash)
                .maxUser(req.getMaxUser())
                .dailyGoal(req.getDailyGoal())
                .createdAt(LocalDate.now())
                .deletedAt(null)
                .build();

        return commGroupRepository.save(group).getGroupId();

    }
}
