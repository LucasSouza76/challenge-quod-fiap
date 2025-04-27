package com.quod.biometric.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "verification_results")
public class VerificationResult {

    @Id
    private String id;
    
    private String userId;
    private VerificationType verificationType;
    private LocalDateTime createdAt;
    private boolean fraudDetected;
    private List<String> fraudTypes;
    private ValidationStatus status;
    private Map<String, Object> metadata;
    private String notificationId;
    private String imageReference;
    
    @Builder.Default
    private LocalDateTime processedAt = LocalDateTime.now();
    
    public enum VerificationType {
        FACIAL_BIOMETRY,
        FINGERPRINT_BIOMETRY,
        DOCUMENT_ANALYSIS
    }
    
    public enum ValidationStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        REJECTED
    }
} 