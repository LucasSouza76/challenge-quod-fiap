package com.quod.biometric.dto;

import com.quod.biometric.model.VerificationResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationResponse {
    private String id;
    private String userId;
    private VerificationResult.VerificationType verificationType;
    private LocalDateTime processedAt;
    private boolean fraudDetected;
    private List<String> fraudTypes;
    private VerificationResult.ValidationStatus status;
    private String message;
} 