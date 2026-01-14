package com.studylog.api.domain.todo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity // DB 테이블과 연결
@Table(name = "todo") // DB 테이블명 명시
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
}
