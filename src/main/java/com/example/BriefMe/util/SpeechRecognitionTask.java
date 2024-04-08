package com.example.BriefMe.util;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpeechRecognitionTask implements Runnable{
    private final int taskId;
    private final int chunkSize;
    private final CountDownLatch latch;
    private final byte[] data;
    private final SpeechClient speechClient;
    public static BlockingQueue<String> resultQueue = new LinkedBlockingQueue<>();

    RecognitionConfig config =
            RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.MP3)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .setEnableAutomaticPunctuation(true)
                    .build();

    public SpeechRecognitionTask(int taskId, CountDownLatch latch, SpeechClient speechClient,
            byte[] data, int chunkSize) {
        this.taskId = taskId;
        this.latch = latch;
        this.data = data;
        this.speechClient = speechClient;
        this.chunkSize = chunkSize;
    }

    @Override
    public void run() {
        // Extract the chunk
        int start = this.taskId * this.chunkSize;
        int end = Math.min((taskId + 1) * this.chunkSize, data.length);

        byte[] chunkData = new byte[end - start];
        System.arraycopy(data, start, chunkData, 0, end - start);
        ByteString chunkBytes = ByteString.copyFrom(chunkData);

        RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(chunkBytes).build();

        log.info("Performing speech to text conversion on the chunk {}", this.taskId);
        RecognizeResponse response = speechClient.recognize(config, audio);
        List<SpeechRecognitionResult> results = response.getResultsList();

        int j = 0;
        StringBuilder transcript = new StringBuilder();
        for (SpeechRecognitionResult result : results) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            log.info(" Chunk {} - Transcription {} : {}", this.taskId , j, alternative.getTranscript());
            transcript.append(alternative.getTranscript());
            j++;
        }

        resultQueue.add(transcript.toString());

        this.latch.countDown();
    }
}
