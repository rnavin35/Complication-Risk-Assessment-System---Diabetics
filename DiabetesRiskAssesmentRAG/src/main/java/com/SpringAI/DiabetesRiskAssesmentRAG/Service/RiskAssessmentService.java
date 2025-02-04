package com.SpringAI.DiabetesRiskAssesmentRAG.Service;

import com.SpringAI.DiabetesRiskAssesmentRAG.Model.Patient;
import com.SpringAI.DiabetesRiskAssesmentRAG.Model.RiskScore;
import com.SpringAI.DiabetesRiskAssesmentRAG.Model.WarningSign;
import groovy.transform.builder.Builder;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class RiskAssessmentService {
    private final OpenAIClient openAIClient;
    private final PatientRepository patientRepository;
    private final ComplicationRiskRepository riskRepository;

    // Risk Categories
    private static final List<String> COMPLICATIONS = Arrays.asList(
            "Nephropathy",
            "Retinopathy",
            "Neuropathy",
            "Cardiovascular",
            "DiabeticFoot"
    );

    public RiskAssessmentReport assessPatientRisk(String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        return generateRiskReport(patient);
    }

    private RiskAssessmentReport generateRiskReport(Patient patient) {
        RiskAssessmentReport report = new RiskAssessmentReport();

        // 1. Analyze Historical Data
        GlucosePatternAnalysis glucosePatterns = analyzeGlucosePatterns(patient);

        // 2. Calculate Risk Scores
        Map<String, RiskScore> riskScores = calculateRiskScores(patient, glucosePatterns);

        // 3. Identify Warning Signs
        List<WarningSign> warningSignsList = identifyWarningSigns(patient, riskScores);

        // 4. Generate Recommendations
        List<String> recommendations = generateRecommendations(riskScores, warningSignsList);

        return report.builder()
                .patientId(patient.getId())
                .assessmentDate(LocalDateTime.now())
                .riskScores(riskScores)
                .warningSigns(warningSignsList)
                .recommendations(recommendations)
                .build();
    }

    private Map<String, RiskScore> calculateRiskScores(Patient patient, GlucosePatternAnalysis patterns) {
        Map<String, RiskScore> scores = new HashMap<>();

        for (String complication : COMPLICATIONS) {
            RiskScore score = calculateComplicationRisk(patient, complication, patterns);
            scores.put(complication, score);
        }

        return scores;
    }

    private RiskScore calculateComplicationRisk(
            Patient patient,
            String complication,
            GlucosePatternAnalysis patterns) {

        List<String> factors = new ArrayList<>();
        double riskScore = 0.0;

        // Base risk factors
        if (patient.getLastHbA1c() > 7.0) {
            riskScore += 0.3;
            factors.add("Elevated HbA1c");
        }

        // Complication-specific risk calculation
        switch (complication) {
            case "Nephropathy" -> {
                if (patient.getVitalSigns().get("bloodPressure") > 140) {
                    riskScore += 0.2;
                    factors.add("High Blood Pressure");
                }
                if (patterns.getHighGlucoseFrequency() > 0.3) {
                    riskScore += 0.25;
                    factors.add("Frequent High Glucose");
                }
            }
            case "Retinopathy" -> {
                if (patient.getLastHbA1c() > 8.0) {
                    riskScore += 0.4;
                    factors.add("Severely Elevated HbA1c");
                }
                if (patterns.getDailyGlucoseVariability() > 50) {
                    riskScore += 0.2;
                    factors.add("High Glucose Variability");
                }
            }
            // Add other complications...
        }

        return RiskScore.builder()
                .complication(complication)
                .score(riskScore)
                .riskLevel(determineRiskLevel(riskScore))
                .contributingFactors(factors)
                .build();
    }

    private List<WarningSign> identifyWarningSigns(
            Patient patient,
            Map<String, RiskScore> riskScores) {

        List<WarningSign> warningSigns = new ArrayList<>();

        // Check for immediate warning signs
        checkVitalSigns(patient, warningSigns);
        checkGlucosePatterns(patient, warningSigns);
        checkSymptomReports(patient, warningSigns);

        // Enhance with AI analysis
        enhanceWarningSignsWithAI(warningSigns, patient, riskScores);

        return warningSigns;
    }

    private void checkVitalSigns(Patient patient, List<WarningSign> warningSigns) {
        Map<String, Double> vitals = patient.getVitalSigns();

        // Blood Pressure Check
        if (vitals.get("systolicBP") > 140 || vitals.get("diastolicBP") > 90) {
            warningSigns.add(WarningSign.builder()
                    .symptom("Elevated Blood Pressure")
                    .relatedComplication("Cardiovascular")
                    .urgencyLevel("HIGH")
                    .recommendedAction("Schedule immediate consultation")
                    .detectionDate(LocalDateTime.now())
                    .build());
        }

        // Other vital sign checks...
    }

    private void enhanceWarningSignsWithAI(
            List<WarningSign> warningSigns,
            Patient patient,
            Map<String, RiskScore> riskScores) {

        String analysisPrompt = """
            Analyze the following diabetes patient data and identified warning signs.
            Suggest any additional warning signs or risk factors that might have been missed.
            
            Patient Data:
            - HbA1c: %f
            - Average Glucose: %f
            - Existing Conditions: %s
            
            Current Warning Signs:
            %s
            
            Risk Scores:
            %s
            
            Provide additional warning signs in a structured format.
            """.formatted(
                patient.getLastHbA1c(),
                patient.getAverageGlucose(),
                String.join(", ", patient.getExistingConditions()),
                formatWarningSigns(warningSigns),
                formatRiskScores(riskScores)
        );

        String aiAnalysis = openAIClient.chatCompletion()
                .create(analysisPrompt)
                .getChoices().get(0).getMessage().getContent();

        List<WarningSign> additionalSigns = parseAIWarningSignsResponse(aiAnalysis);
        warningSigns.addAll(additionalSigns);
    }

    private List<String> generateRecommendations(
            Map<String, RiskScore> riskScores,
            List<WarningSign> warningSigns) {

        List<String> recommendations = new ArrayList<>();

        // Priority-based recommendations
        recommendationsForHighRisks(riskScores, recommendations);
        recommendationsForWarningSigns(warningSigns, recommendations);
        recommendPreventiveMeasures(riskScores, recommendations);

        return recommendations;
    }

    private void recommendationsForHighRisks(
            Map<String, RiskScore> riskScores,
            List<String> recommendations) {

        riskScores.forEach((complication, score) -> {
            if (score.getScore() > 0.7) {
                recommendations.add(String.format(
                        "URGENT: Schedule immediate consultation for %s risk. " +
                                "Contributing factors: %s",
                        complication,
                        String.join(", ", score.getContributingFactors())
                ));
            } else if (score.getScore() > 0.5) {
                recommendations.add(String.format(
                        "HIGH PRIORITY: Monitor %s closely. " +
                                "Recommended preventive measures: %s",
                        complication,
                        getPreventiveMeasures(complication)
                ));
            }
        });
    }
}
