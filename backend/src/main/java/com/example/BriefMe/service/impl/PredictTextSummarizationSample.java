package com.example.BriefMe.service.impl;

import com.example.BriefMe.properties.VertexAIProperties;
import com.example.BriefMe.request.VertexAIParameters;
import com.example.BriefMe.request.VertexAIData;
import com.example.BriefMe.service.client.TextSummarizer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.print.DocFlavor.STRING;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class PredictTextSummarizationSample implements TextSummarizer {

    //TODO: Set vaules in application.properties
    @Autowired
    VertexAIProperties vertexAIProperties;
    @Override
    public String generateSummary(String text, int numberOfLines) {
        try{
            String prompt = createPromptString(text, numberOfLines);
            String parameters= createParametersString();

            String endpoint = String.format("%s-aiplatform.googleapis.com:443", vertexAIProperties.getLocation());
            PredictionServiceSettings predictionServiceSettings =
                    PredictionServiceSettings.newBuilder()
                            .setEndpoint(endpoint)
                            .build();

            // Initialize client
            try (PredictionServiceClient predictionServiceClient =
                    PredictionServiceClient.create(predictionServiceSettings)) {
                final EndpointName endpointName =
                        EndpointName.ofProjectLocationPublisherModelName(
                                vertexAIProperties.getProject(),
                                vertexAIProperties.getLocation(),
                                vertexAIProperties.getPublisher(),
                                vertexAIProperties.getModel());

                // Use Value.Builder to convert prompt to a dynamically typed value
                Value.Builder instanceValue = Value.newBuilder();
                JsonFormat.parser().merge(prompt, instanceValue);
                List<Value> instances = new ArrayList<>();
                instances.add(instanceValue.build());

                // Use Value.Builder to convert parameter to a dynamically typed value
                Value.Builder parameterValueBuilder = Value.newBuilder();
                JsonFormat.parser().merge(parameters, parameterValueBuilder);
                Value parameterValue = parameterValueBuilder.build();

                PredictResponse predictResponse =
                        predictionServiceClient.predict(endpointName, instances, parameterValue);

                //TODO: Fetch and return data
                System.out.println("Predict Response");
                System.out.println(predictResponse);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Nothing to summarize";
    }

    private String createPromptString(String text, int numberOfLines){
        try{
            String prompt = "Provide a short summary in "+ numberOfLines +" numeric bullet points:" + text;
            VertexAIData vertexAIData = new VertexAIData(prompt);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(vertexAIData);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred while creating prompt string {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String createParametersString(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            VertexAIParameters vertexAIParameters = new VertexAIParameters(
                    vertexAIProperties.getTemperature(),
                    vertexAIProperties.getMaxOutputToken(),
                    vertexAIProperties.getTopP(),
                    vertexAIProperties.getTopK());
            return objectMapper.writeValueAsString(vertexAIParameters);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred while creating parameters string {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
