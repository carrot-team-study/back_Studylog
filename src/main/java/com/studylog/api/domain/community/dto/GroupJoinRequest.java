package com.studylog.api.domain.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupJoinRequest {

    private String password; //공개방이면 null
}
