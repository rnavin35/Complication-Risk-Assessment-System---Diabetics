package com.SpringAI.DiabetesRiskAssesmentRAG.Model;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class WarningSign {
    private String symptom;
    private String relatedComplication;
    private String urgencyLevel;
    private String recommendedAction;
    private LocalDateTime detectionDate;
}
