package com.example.BriefMe.service.client;

public interface VideoToTextConverter {
    String fetchSubtitlesJsonFileFromVideo(String video);
    String readSubtitlesFromJsonFile(String filePath);
}
