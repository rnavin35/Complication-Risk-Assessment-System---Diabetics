package com.SpringAI.DiabetesRiskAssesmentRAG.Model;

import lombok.Data;
import lombok.Builder;

import java.util.List;


@Data
@Builder
public class RiskScore {
    private String complication;
    private Double score; // 0-1
    private String riskLevel; // LOW, MODERATE, HIGH, SEVERE
    private List<String> contributingFactors;
}