package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.AudioExtractor;
import com.example.BriefMe.service.client.AudioToTextConverter;
import com.example.BriefMe.service.client.TextSummarizer;
import com.example.BriefMe.service.client.YoutubeVideoSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YoutubeVideoSummaryServiceImpl implements YoutubeVideoSummaryService {

    @Autowired
    AudioExtractor audioExtractor;

    @Autowired
    AudioToTextConverter audioToTextConverter;

    @Autowired
    TextSummarizer textSummarizer;

    @Override
    public String generateSummary(String youtubeVideoUrl, int summarizeIn) {
        String audioFile = audioExtractor.extractAudio(youtubeVideoUrl);
        String text = audioToTextConverter.covertAudioToText(audioFile);
        String summary = textSummarizer.generateSummary(text, summarizeIn);

        log.info("Text summarization completed...");

        return  summary;
    }
}
