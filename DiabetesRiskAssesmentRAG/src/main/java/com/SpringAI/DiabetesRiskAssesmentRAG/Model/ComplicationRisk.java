package com.SpringAI.DiabetesRiskAssesmentRAG.Model;

import java.util.List;
import java.util.Map;

@Node
public class ComplicationRisk {
    @Id
    private String type; // e.g., "Nephropathy", "Retinopathy"
    private List<String> earlyWarningSymptoms;
    private List<String> riskFactors;
    private Map<String, Double> thresholds;
    private String preventiveMeasures;

    // getters, setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getEarlyWarningSymptoms() {
        return earlyWarningSymptoms;
    }

    public void setEarlyWarningSymptoms(List<String> earlyWarningSymptoms) {
        this.earlyWarningSymptoms = earlyWarningSymptoms;
    }

    public List<String> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<String> riskFactors) {
        this.riskFactors = riskFactors;
    }

    public Map<String, Double> getThresholds() {
        return thresholds;
    }

    public void setThresholds(Map<String, Double> thresholds) {
        this.thresholds = thresholds;
    }

    public String getPreventiveMeasures() {
        return preventiveMeasures;
    }

    public void setPreventiveMeasures(String preventiveMeasures) {
        this.preventiveMeasures = preventiveMeasures;
    }
}

