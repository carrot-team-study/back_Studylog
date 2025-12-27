package com.studylog.api.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//ci 테스트22
@RestController
public class DbTestController {

	private final JdbcTemplate jdbcTemplate;

    public DbTestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/db/ping")
    public String ping() {
        return jdbcTemplate.queryForObject("select 'ok'", String.class);
    }
    
    @GetMapping("/api/db/health")
    public String health() {
        return jdbcTemplate.queryForObject(
            "select 1",
            String.class
        );
    }

}
