package com.example.BriefMe;

import com.example.BriefMe.service.client.AudioExtractor;
import com.example.BriefMe.service.client.AudioToTextConverter;
import com.example.BriefMe.service.client.TextSummarizer;
import com.example.BriefMe.service.impl.AudioToTextConverterImpl;
import com.example.BriefMe.service.impl.CustomTextSummarizer;
import com.example.BriefMe.service.impl.YoutubeAudioExtractorImpl;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BriefMeApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(BriefMeApplication.class, args);
	}
}
