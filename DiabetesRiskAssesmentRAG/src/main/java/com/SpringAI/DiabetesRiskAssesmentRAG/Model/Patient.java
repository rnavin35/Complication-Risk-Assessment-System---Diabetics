package com.SpringAI.DiabetesRiskAssesmentRAG.Model;

import java.util.List;
import java.util.Map;

@Node
public class Patient {
    @Id
    private String id;
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Double getLastHbA1c() {
        return lastHbA1c;
    }

    public void setLastHbA1c(Double lastHbA1c) {
        this.lastHbA1c = lastHbA1c;
    }

    public Double getAverageGlucose() {
        return averageGlucose;
    }

    public void setAverageGlucose(Double averageGlucose) {
        this.averageGlucose = averageGlucose;
    }

    public List<String> getExistingConditions() {
        return existingConditions;
    }

    public void setExistingConditions(List<String> existingConditions) {
        this.existingConditions = existingConditions;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public Map<String, Double> getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(Map<String, Double> vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    private Double bmi;
    private Double lastHbA1c;
    private Double averageGlucose;
    private List<String> existingConditions;
    private List<String> medications;
    private Map<String, Double> vitalSigns;

}
