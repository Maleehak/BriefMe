package com.example.BriefMe.service.client;

public interface YoutubeVideoSummaryService {
    String generateSummary(String youtubeVideoUrl, int summarizeIn);
}
