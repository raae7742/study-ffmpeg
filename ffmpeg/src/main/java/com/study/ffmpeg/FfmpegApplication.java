package com.study.ffmpeg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FfmpegApplication {

	public static void main(String[] args) {
		SpringApplication.run(FfmpegApplication.class, args);
	}

}
