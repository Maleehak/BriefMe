package com.example.BriefMe.service.client;

import java.io.IOException;

public interface AudioToTextConverter {
    void covertAudioToText(String audioFile) throws IOException;
}
