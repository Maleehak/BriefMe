package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.AudioToTextConverter;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudioToTextConverterImpl implements AudioToTextConverter {

    public String covertAudioToText(String audioFile) throws IOException {

        try (SpeechClient speechClient = SpeechClient.create()) {

            log.info("Loading the audio file into memory...");

            Path path = Paths.get(audioFile);
            byte[] data = Files.readAllBytes(path);
            log.info("Audio file loaded...");

            int chunkSize = 2097152; // 2MB
            int numChunks = (int) Math.ceil((double) data.length / chunkSize);

            log.info("Audio file is divided into {} chunks", numChunks);

            // Builds the sync recognize request
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(AudioEncoding.MP3)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .setEnableAutomaticPunctuation(true)
                            .build();

            String completeTranscript = "";

            for (int i = 0; i < numChunks; i++) {

                // Extract the current chunk
                int start = i * chunkSize;
                int end = Math.min((i + 1) * chunkSize, data.length);
                byte[] chunkData = new byte[end - start];
                System.arraycopy(data, start, chunkData, 0, end - start);
                ByteString chunkBytes = ByteString.copyFrom(chunkData);

                RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(chunkBytes).build();

                log.info("Performing speech to text conversion on the chunk {}", i);
                RecognizeResponse response = speechClient.recognize(config, audio);
                List<SpeechRecognitionResult> results = response.getResultsList();

                int j = 0;
                for (SpeechRecognitionResult result : results) {
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    log.info(" Chunk {} - Transcription {} : {}",i, j, alternative.getTranscript());
                    completeTranscript = completeTranscript + alternative.getTranscript();
                    j++;
                }
            }

            // TODO: Delete the audio file after the text has been read

            log.info("Audio conversion to text completed. Transcript: {}", completeTranscript);
            return completeTranscript;
        }
    }
}
