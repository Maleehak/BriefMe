package com.example.BriefMe;

import com.example.BriefMe.service.impl.PredictTextSummarizationSample;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class BriefMeApplication {
	public static void main(String[] args) throws IOException {

		SpringApplication.run(BriefMeApplication.class, args);
		PredictTextSummarizationSample predictTextSummarization = new PredictTextSummarizationSample();
		predictTextSummarization.predict();

	}
}
