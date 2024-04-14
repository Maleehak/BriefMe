package com.example.BriefMe.service;

import com.example.BriefMe.service.client.AudioExtractor;
import com.example.BriefMe.service.client.AudioToTextConverter;
import com.example.BriefMe.service.client.TextSummarizer;
import com.example.BriefMe.service.client.VideoToTextConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YoutubeVideoSummaryService {

    @Autowired
    AudioExtractor audioExtractor;

    @Autowired
    AudioToTextConverter audioToTextConverter;

    @Autowired
    TextSummarizer textSummarizer;

    @Autowired
    VideoToTextConverter videoToTextConverter;

    public String generateSummary(String youtubeVideoUrl, int summarizeIn) {
        String audioFile = audioExtractor.extractAudio(youtubeVideoUrl);
        String text = audioToTextConverter.covertAudioToText(audioFile);
        String summary = textSummarizer.generateSummary(text, summarizeIn);

        log.info("Text summarization completed using custom text summarizer ...");

        return  summary;
    }

    public String generateSummaryFromSubtitles(String youtubeVideoUrl, int summarizeIn) {
        String subtitlesFile = videoToTextConverter.fetchSubtitlesJsonFileFromVideo(youtubeVideoUrl);
        String subtitles = videoToTextConverter.readSubtitlesFromJsonFile(subtitlesFile);
        String summary = textSummarizer.generateSummary(subtitles, summarizeIn);

        log.info("Text summarization completed using Goggle generative text summarizer...");
        return  summary;
    }
}
