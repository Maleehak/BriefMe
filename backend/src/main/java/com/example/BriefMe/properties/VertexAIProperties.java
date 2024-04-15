package com.example.BriefMe.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class VertexAIProperties {

    @Value("${google.vertex-ai.project}")
    String project;

    @Value("${google.vertex-ai.location}")
    String location;

    @Value("${google.vertex-ai.publisher}")
    String publisher;

    @Value("${google.vertex-ai.model}")
    String model;

    @Value("${google.vertex-ai.model.temperature}")
    Double temperature;

    @Value("${google.vertex-ai.model.max-output-token}")
    Integer maxOutputToken;

    @Value("${google.vertex-ai.model.top-p}")
    Double topP;

    @Value("${google.vertex-ai.model.top-k}")
    Integer topK;
}
