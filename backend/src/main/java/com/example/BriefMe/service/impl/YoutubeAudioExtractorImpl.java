package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.AudioExtractor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
public class YoutubeAudioExtractorImpl implements AudioExtractor {

    public static final String OUTPUT_FILE = "output-";
    public static final String DOT = ".";
    public static final String MP_3 = "mp3";

    @Override
    public String extractAudio(String inputVideoFile) {
        String outputFile = OUTPUT_FILE + UUID.randomUUID() + DOT;
        String defaultSearch = "ytsearch";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

        try {
            String[] command = {
                    "youtube-dl",
                    "--extract-audio",
                    "--default-search",
                    defaultSearch,
                    "--audio-format",
                    MP_3,
                    "--user-agent",
                    userAgent,
                    "-o",
                    outputFile + "%(ext)s",
                    "'" + inputVideoFile + "'"
            };

            // Execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output of the command
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Audio downloaded successfully.");
            } else {
                log.error("Failed to download audio.");
            }

            // When YouTube video is being downloaded, output is first converted to m4a and then finally converted to mp3
            // Giving the output file in mp3 version directly in the command will corrupt the audio
            // Once the audio file is created, we can return it as mp3
            return outputFile + MP_3;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return outputFile + MP_3;
    }
}
