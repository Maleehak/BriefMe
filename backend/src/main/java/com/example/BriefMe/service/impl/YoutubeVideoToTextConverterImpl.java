package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.VideoToTextConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class YoutubeVideoToTextConverterImpl implements VideoToTextConverter {

    public static final String OUTPUT_FILE = "output-";
    public static final String DOT = ".";

    public static final String MP4 = "mp4";
    public static final String SUBTITLES_FORMAT = "json3";

    @Override
    public String fetchSubtitlesJsonFileFromVideo(String inputVideo) {
        String outputFile = OUTPUT_FILE + UUID.randomUUID();
        String subtitlesLanguage = "en";

        try {
            String[] command = {
                    "yt-dlp",
                    "--skip-download",
                    "--write-subs",
                    "--write-auto-subs",
                    "--sub-langs",
                    subtitlesLanguage,
                    "--sub-format",
                    SUBTITLES_FORMAT,
                    "--sleep-interval",
                    String.valueOf(1),
                    inputVideo,
                    "-o",
                    outputFile + "%(ext)s"
            };

            // Execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Subtitles downloaded successfully.");
            } else {
                log.error("Failed to download subtitles.");
            }

            return outputFile + MP4 + DOT + subtitlesLanguage + DOT + SUBTITLES_FORMAT;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return outputFile + MP4 + DOT + subtitlesLanguage + DOT + SUBTITLES_FORMAT;
    }

    @Override
    public String readSubtitlesFromJsonFile(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StringBuilder subtitles = new StringBuilder();

            File jsonFile = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(jsonFile);

            // Extract "segs" arrays from "events"
            List<JsonNode> segsList = new ArrayList<>();
            JsonNode eventsNode = rootNode.get("events");
            if (eventsNode != null && eventsNode.isArray()) {
                for (JsonNode eventNode : eventsNode) {
                    JsonNode segsNode = eventNode.get("segs");
                    if (segsNode != null && segsNode.isArray()) {
                        // Iterate over the "segs" array and extract the "utf8" values
                        for (JsonNode segNode : segsNode) {
                            JsonNode utf8Node = segNode.get("utf8");
                            if (utf8Node != null) {
                                String utf8Value = utf8Node.asText();
                                // Check if the "utf8" value contains "\n"
                                if (Objects.equals(utf8Value, "\n")) {
                                    subtitles.append(" ");
                                }else{
                                    subtitles.append(utf8Value);
                                }
                            }
                        }
                    }
                }
            }
            log.info("Subtitles extracted...");
            Files.deleteIfExists(Path.of(filePath));

            return subtitles.toString();

        } catch (Exception e) {
            log.error("Exception occurred while trying to extract subtitles: {}", e.getMessage());
            e.printStackTrace();
        }
        return "";
    }



}
