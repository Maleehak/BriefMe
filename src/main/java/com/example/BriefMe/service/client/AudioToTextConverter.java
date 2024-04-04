package com.example.BriefMe.service.client;

import java.io.IOException;

public interface AudioToTextConverter {
    String covertAudioToText(String audioFile) throws IOException;
}
