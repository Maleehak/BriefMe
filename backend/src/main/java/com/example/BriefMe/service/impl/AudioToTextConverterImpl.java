package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.AudioToTextConverter;
import com.example.BriefMe.util.SpeechRecognitionTask;
import com.google.cloud.speech.v1.SpeechClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AudioToTextConverterImpl implements AudioToTextConverter {

    public String covertAudioToText(String audioFile){

        try (SpeechClient speechClient = SpeechClient.create()) {

            log.info("Loading the audio file into memory...");

            Path path = Path.of(audioFile);
            byte[] data = Files.readAllBytes(path);
            log.info("Audio file loaded...");

            int chunkSize = 2097152; // 2MB
            int numChunks = (int) Math.ceil((double) data.length / chunkSize);

            log.info("Audio file is divided into {} chunks", numChunks);

            StringBuilder completeTranscript = new StringBuilder();

            CountDownLatch latch = new CountDownLatch(numChunks);

            ExecutorService executor = Executors.newFixedThreadPool(numChunks);
            for (int i = 0; i < numChunks; i++) {
                executor.submit(new SpeechRecognitionTask(i, latch, speechClient, data, chunkSize));
            }

            try {
                // Wait for all threads to complete
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < numChunks; i++) {
                completeTranscript.append(SpeechRecognitionTask.resultQueue.poll());
            }

            executor.shutdown();

            log.info("Audio conversion to text completed");

            Files.deleteIfExists(path);

            return completeTranscript.toString();
        } catch (IOException e) {
            log.error("Exception occurred with audio to text conversion: {}", e.getMessage());
            return "";
        }
    }
}
