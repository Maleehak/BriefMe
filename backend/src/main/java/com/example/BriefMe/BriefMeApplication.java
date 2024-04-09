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

		AudioExtractor audioExtractor = new YoutubeAudioExtractorImpl();
		String audioFile = audioExtractor.extractAudio("https://www.youtube.com/watch?v=Fkd9TWUtFm0");

		AudioToTextConverter audioToTextConverter = new AudioToTextConverterImpl();
		String text = audioToTextConverter.covertAudioToText(audioFile);

		TextSummarizer textSummarizer = new CustomTextSummarizer();
		int numberOfLines = 5;
		String summary = textSummarizer.generateSummary(text, numberOfLines);

		log.info("Text summarization completed...");
		log.info("Summary: {}", summary);
	}
}
