package com.studylog.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudylogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudylogApiApplication.class, args);
	}
	@GetMapping("/study-check")
	public String studyCheck() {
		return "STUDYLOG SERVER OK";
		// CI flow test comment
	}

}
