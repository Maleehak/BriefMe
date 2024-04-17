package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.AudioExtractor;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Mp4AudioExtractorImpl implements AudioExtractor {

    public static final String OUTPUT_FILE = "output-";
    public static final String DOT = ".";
    public static final String MP_3 = "mp3";

    @Override
    public String extractAudio(String inputVideoFile) {
        try{
            FFmpeg ffmpeg = new FFmpeg("ffmpeg");
            FFprobe ffprobe = new FFprobe();

            String outputFile = OUTPUT_FILE + UUID.randomUUID() + DOT + MP_3;

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputVideoFile)
                    .overrideOutputFiles(true)
                    .addOutput(outputFile)
                    .setFormat(MP_3)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();

            return outputFile;
        }catch(Exception e){
            log.error("Unable to extract audio from the link. Exception occured. {}", e.getMessage());
            return "";
        }
    }
}
