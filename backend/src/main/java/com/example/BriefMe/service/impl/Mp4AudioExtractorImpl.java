package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.AudioExtractor;
import java.io.IOException;
import java.util.UUID;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class Mp4AudioExtractorImpl implements AudioExtractor {

    public static final String OUTPUT_FILE = "output-";
    public static final String DOT = ".";
    public static final String MP_3 = "mp3";

    @Override
    public String extractAudio(String inputVideoFile) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("/opt/homebrew/bin/ffmpeg");
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
    }
}
