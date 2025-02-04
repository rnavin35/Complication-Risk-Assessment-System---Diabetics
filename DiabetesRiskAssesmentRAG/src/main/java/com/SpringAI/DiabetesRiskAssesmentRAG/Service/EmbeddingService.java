package com.SpringAI.DiabetesRiskAssesmentRAG.Service;

import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {
    private final OpenAiService openAiService;

    public List<Double> generateEmbedding(String text) {
        EmbeddingRequest request = new EmbeddingRequest()
                .setModel("text-embedding-ada-002")
                .setInput(text);

        EmbeddingResponse response = openAiService.createEmbeddings(request);
        return response.getData().get(0).getEmbedding();
    }

    public void embedPatientData(Patient patient) {
        // Generate embeddings for patient description
        String patientDescription = String.format(
                "Patient age %d with BMI %.2f and risk score %.2f",
                patient.getAge(),
                patient.getBmi(),
                patient.getRiskScore()
        );

        List<Double> embedding = generateEmbedding(patientDescription);
        patient.setEmbedding(embedding);
    }
}