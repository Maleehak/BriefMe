package com.example.BriefMe.service.impl;

import com.example.BriefMe.request.VertexAIParameters;
import com.example.BriefMe.request.VertexAIPrompt;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.stereotype.Service;
import com.google.cloud.aiplatform.v1beta1.EndpointName;
import com.google.cloud.aiplatform.v1beta1.PredictResponse;
import com.google.cloud.aiplatform.v1beta1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1beta1.PredictionServiceSettings;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class PredictTextSummarizationSample {
    public void predict() throws IOException {
        String text ="";
        VertexAIPrompt vertexAIPrompt = new VertexAIPrompt("Provide a short summary in five numeric bullet points:" + text);
        ObjectMapper objectMapper = new ObjectMapper();
        String vertexAIRequestString = objectMapper.writeValueAsString(vertexAIPrompt);

        VertexAIParameters vertexAIParameters = new VertexAIParameters(0.2, 256, 0.95, 40);
        String vertexAIParamsString = objectMapper.writeValueAsString(vertexAIParameters);

        
        String project = "artful-lane-419217";
        String location = "us-central1";
        String publisher = "google";
        String model = "text-bison@001";

        predictTextSummarization(vertexAIRequestString, vertexAIParamsString, project, location, publisher, model);
    }

    // Get summarization from a supported text model
    public void predictTextSummarization(
            String instance,
            String parameters,
            String project,
            String location,
            String publisher,
            String model)
            throws IOException {
        String endpoint = String.format("%s-aiplatform.googleapis.com:443", location);

        PredictionServiceSettings predictionServiceSettings =
                PredictionServiceSettings.newBuilder()
                        .setEndpoint(endpoint)
                        .build();

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests.
        try (PredictionServiceClient predictionServiceClient =
                PredictionServiceClient.create(predictionServiceSettings)) {
            final EndpointName endpointName =
                    EndpointName.ofProjectLocationPublisherModelName(project, location, publisher, model);

            // Use Value.Builder to convert instance to a dynamically typed value that can be
            // processed by the service.
            Value.Builder instanceValue = Value.newBuilder();
            JsonFormat.parser().merge(instance, instanceValue);
            List<Value> instances = new ArrayList<>();
            instances.add(instanceValue.build());

            // Use Value.Builder to convert parameter to a dynamically typed value that can be
            // processed by the service.
            Value.Builder parameterValueBuilder = Value.newBuilder();
            JsonFormat.parser().merge(parameters, parameterValueBuilder);
            Value parameterValue = parameterValueBuilder.build();

            PredictResponse predictResponse =
                    predictionServiceClient.predict(endpointName, instances, parameterValue);

            System.out.println("Predict Response");
            System.out.println(predictResponse);
        }
    }

}
