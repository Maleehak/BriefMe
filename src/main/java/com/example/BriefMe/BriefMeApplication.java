package com.example.BriefMe;

import com.example.BriefMe.service.client.AudioExtractor;
import com.example.BriefMe.service.impl.Mp4AudioExtractorImpl;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BriefMeApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(BriefMeApplication.class, args);
		AudioExtractor audioExtractor = new Mp4AudioExtractorImpl();
		audioExtractor.extractAudio("ESP-CMS-KT.mp4");

	}
}
