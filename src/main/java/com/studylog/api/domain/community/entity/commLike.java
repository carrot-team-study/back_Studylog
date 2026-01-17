package com.studylog.api.domain.community.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comm_like")
public class commLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private long likeID;

    @Column(name = "group_id", nullable = false)
    private long groupID;

    @Column(name = "from_user_id", nullable = false)
    private long fromUserID;

    @Column(name = "to_user_id", nullable = false)
    private long toUserId;

}

