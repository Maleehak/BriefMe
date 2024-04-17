package com.example.BriefMe.service.impl;

import com.example.BriefMe.properties.VertexAIProperties;
import com.example.BriefMe.data.request.VertexAIParameters;
import com.example.BriefMe.data.request.VertexAIData;
import com.example.BriefMe.service.client.TextSummarizer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileInputStream;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.cloud.aiplatform.v1beta1.EndpointName;
import com.google.cloud.aiplatform.v1beta1.PredictResponse;
import com.google.cloud.aiplatform.v1beta1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1beta1.PredictionServiceSettings;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GoogleVertexAiTextSummarizer implements TextSummarizer {
    @org.springframework.beans.factory.annotation.Value("${google.api.credentials.location}")
    private String credentialsPath;

    @Autowired
    VertexAIProperties vertexAIProperties;
    @Override
    public String generateSummary(String text, int numberOfLines) {
        try{

            // create credentials using google service account json
            List<String> scopes = Arrays.asList("https://www.googleapis.com/auth/cloud-platform",
                    "https://www.googleapis.com/auth/cloud-platform.read-only");


            ServiceAccountCredentials serviceAccountCredentials = (ServiceAccountCredentials) ServiceAccountCredentials
                    .fromStream(new FileInputStream(credentialsPath))
                    .createScoped(scopes);

            CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(serviceAccountCredentials);


            String endpoint = String.format("%s-aiplatform.googleapis.com:443", vertexAIProperties.getLocation());
            PredictionServiceSettings predictionServiceSettings =
                    PredictionServiceSettings.newBuilder()
                            .setEndpoint(endpoint)
                            .setCredentialsProvider(credentialsProvider)
                            .build();

            //setup request params
            String prompt = createPromptString(text, numberOfLines);
            String parameters= createParametersString();

            // Initialize client
            try (PredictionServiceClient predictionServiceClient = PredictionServiceClient.create(predictionServiceSettings)) {
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

                PredictResponse predictResponse = predictionServiceClient.predict(endpointName, instances, parameterValue);
                return predictResponse.getPredictions(0).getStructValue().getFieldsOrThrow("content").getStringValue();
            }
        } catch (Exception e) {
            log.error("Unable to get predictions from Google Vertex AI. {}", e.getMessage());
            return "Nothing to summarize";
        }

    }

    private String createPromptString(String text, int numberOfLines){
        try{
            String prompt = "Provide a short summary in "+ numberOfLines +" numeric bullet points:" + text;
            log.info("Prompt: {}", prompt);
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
