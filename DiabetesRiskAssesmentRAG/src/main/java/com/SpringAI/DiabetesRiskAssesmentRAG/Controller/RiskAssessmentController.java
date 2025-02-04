package com.SpringAI.DiabetesRiskAssesmentRAG.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diabetes")
public class RiskAssessmentController {
    private final RiskAssessmentService riskService;

    @GetMapping("/risk-assessment/{patientId}")
    public ResponseEntity<RiskAssessmentReport> getPatientRiskAssessment(
            @PathVariable String patientId) {
        RiskAssessmentReport report = riskService.assessPatientRisk(patientId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/warning-signs/{patientId}")
    public ResponseEntity<List<WarningSign>> getPatientWarningSigns(
            @PathVariable String patientId) {
        List<WarningSign> warningSigns = riskService.getCurrentWarningSigns(patientId);
        return ResponseEntity.ok(warningSigns);
    }
}
