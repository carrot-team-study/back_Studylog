package com.studylog.api.domain.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CommGroupTagId implements Serializable {

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "tag_id")
    private Long tagId;
}
