package com.quod.biometric.service;

import com.quod.biometric.dto.DocumentAnalysisRequest;
import com.quod.biometric.dto.FacialBiometryRequest;
import com.quod.biometric.dto.FingerprintBiometryRequest;
import com.quod.biometric.dto.VerificationResponse;
import com.quod.biometric.model.VerificationResult;
import com.quod.biometric.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BiometricVerificationService {

    private final ImageValidationService imageValidationService;
    private final FraudDetectionService fraudDetectionService;
    private final NotificationService notificationService;
    private final VerificationRepository verificationRepository;

    /**
     * Process a facial biometry verification request
     * @param request The facial biometry request
     * @return Verification response
     */
    public VerificationResponse processFacialBiometry(FacialBiometryRequest request) {
        log.info("Processing facial biometry for user ID: {}", request.getUserId());
        
        // Step 1: Basic image validation
        Map<String, Object> imageValidation = imageValidationService.validateImage(request.getFaceImage());
        if (!(boolean) imageValidation.get("valid")) {
            return createRejectedResponse(request.getUserId(), 
                    VerificationResult.VerificationType.FACIAL_BIOMETRY,
                    "Image validation failed: " + imageValidation.get("errors"));
        }
        
        // Step 2: Fraud detection
        Map<String, Object> fraudDetection = fraudDetectionService.detectFacialFraud(request.getFaceImage());
        boolean fraudDetected = (boolean) fraudDetection.get("fraudDetected");
        
        // Step 3: Create verification result
        VerificationResult result = createVerificationResult(
                request.getUserId(),
                VerificationResult.VerificationType.FACIAL_BIOMETRY,
                fraudDetected,
                (ArrayList<String>) fraudDetection.get("fraudTypes"),
                fraudDetected ? VerificationResult.ValidationStatus.REJECTED : VerificationResult.ValidationStatus.APPROVED,
                (Map<String, Object>) imageValidation.get("metadata")
        );
        
        // Add device info and geolocation if provided
        if (request.getDeviceInfo() != null) {
            result.getMetadata().put("deviceInfo", request.getDeviceInfo());
        }
        if (request.getGeoLocation() != null) {
            result.getMetadata().put("geoLocation", request.getGeoLocation());
        }
        
        // Step 4: Save result
        result = verificationRepository.save(result);
        
        // Step 5: Notify if fraud detected or send success notification
        if (fraudDetected) {
            String notificationId = notificationService.sendFraudNotification(result);
            result.setNotificationId(notificationId);
            verificationRepository.save(result);
        } else {
            String notificationId = notificationService.sendSuccessNotification(result);
            result.setNotificationId(notificationId);
            verificationRepository.save(result);
        }
        
        return mapToResponse(result);
    }
    
    /**
     * Process a fingerprint biometry verification request
     * @param request The fingerprint biometry request
     * @return Verification response
     */
    public VerificationResponse processFingerprintBiometry(FingerprintBiometryRequest request) {
        log.info("Processing fingerprint biometry for user ID: {}", request.getUserId());
        
        // Step 1: Basic image validation
        Map<String, Object> imageValidation = imageValidationService.validateImage(request.getFingerprintImage());
        if (!(boolean) imageValidation.get("valid")) {
            return createRejectedResponse(request.getUserId(), 
                    VerificationResult.VerificationType.FINGERPRINT_BIOMETRY,
                    "Image validation failed: " + imageValidation.get("errors"));
        }
        
        // Step 2: Fraud detection
        Map<String, Object> fraudDetection = fraudDetectionService.detectFingerprintFraud(request.getFingerprintImage());
        boolean fraudDetected = (boolean) fraudDetection.get("fraudDetected");
        
        // Step 3: Create verification result
        Map<String, Object> metadata = new HashMap<>((Map<String, Object>) imageValidation.get("metadata"));
        metadata.put("fingerPosition", request.getFingerPosition());
        
        VerificationResult result = createVerificationResult(
                request.getUserId(),
                VerificationResult.VerificationType.FINGERPRINT_BIOMETRY,
                fraudDetected,
                (ArrayList<String>) fraudDetection.get("fraudTypes"),
                fraudDetected ? VerificationResult.ValidationStatus.REJECTED : VerificationResult.ValidationStatus.APPROVED,
                metadata
        );
        
        // Add device info and geolocation if provided
        if (request.getDeviceInfo() != null) {
            result.getMetadata().put("deviceInfo", request.getDeviceInfo());
        }
        if (request.getGeoLocation() != null) {
            result.getMetadata().put("geoLocation", request.getGeoLocation());
        }
        
        // Step 4: Save result
        result = verificationRepository.save(result);
        
        // Step 5: Notify if fraud detected
        if (fraudDetected) {
            String notificationId = notificationService.sendFraudNotification(result);
            result.setNotificationId(notificationId);
            verificationRepository.save(result);
        } else {
            String notificationId = notificationService.sendSuccessNotification(result);
            result.setNotificationId(notificationId);
            verificationRepository.save(result);
        }
        
        return mapToResponse(result);
    }
    
    /**
     * Process a document analysis verification request
     * @param request The document analysis request
     * @return Verification response
     */
    public VerificationResponse processDocumentAnalysis(DocumentAnalysisRequest request) {
        log.info("Processing document analysis for user ID: {}", request.getUserId());
        
        // Step 1: Basic image validation for document
        Map<String, Object> documentImageValidation = imageValidationService.validateImage(request.getDocumentImage());
        if (!(boolean) documentImageValidation.get("valid")) {
            return createRejectedResponse(request.getUserId(), 
                    VerificationResult.VerificationType.DOCUMENT_ANALYSIS,
                    "Document image validation failed: " + documentImageValidation.get("errors"));
        }
        
        // Step 2: Basic image validation for face
        Map<String, Object> faceImageValidation = imageValidationService.validateImage(request.getFaceImage());
        if (!(boolean) faceImageValidation.get("valid")) {
            return createRejectedResponse(request.getUserId(), 
                    VerificationResult.VerificationType.DOCUMENT_ANALYSIS,
                    "Face image validation failed: " + faceImageValidation.get("errors"));
        }
        
        // Step 3: Fraud detection
        Map<String, Object> fraudDetection = fraudDetectionService.detectDocumentFraud(
                request.getDocumentImage(), request.getFaceImage());
        boolean fraudDetected = (boolean) fraudDetection.get("fraudDetected");
        
        // Step 4: Create verification result
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("documentMetadata", documentImageValidation.get("metadata"));
        metadata.put("faceMetadata", faceImageValidation.get("metadata"));
        metadata.put("documentType", request.getDocumentType());
        
        VerificationResult result = createVerificationResult(
                request.getUserId(),
                VerificationResult.VerificationType.DOCUMENT_ANALYSIS,
                fraudDetected,
                (ArrayList<String>) fraudDetection.get("fraudTypes"),
                fraudDetected ? VerificationResult.ValidationStatus.REJECTED : VerificationResult.ValidationStatus.APPROVED,
                metadata
        );
        
        // Add device info and geolocation if provided
        if (request.getDeviceInfo() != null) {
            result.getMetadata().put("deviceInfo", request.getDeviceInfo());
        }
        if (request.getGeoLocation() != null) {
            result.getMetadata().put("geoLocation", request.getGeoLocation());
        }
        
        // Step 5: Save result
        result = verificationRepository.save(result);
        
        // Step 6: Notify if fraud detected
        if (fraudDetected) {
            String notificationId = notificationService.sendFraudNotification(result);
            result.setNotificationId(notificationId);
            verificationRepository.save(result);
        } else {
            String notificationId = notificationService.sendSuccessNotification(result);
            result.setNotificationId(notificationId);
            verificationRepository.save(result);
        }
        
        return mapToResponse(result);
    }
    
    /**
     * Helper method to create a verification result
     */
    private VerificationResult createVerificationResult(
            String userId,
            VerificationResult.VerificationType verificationType,
            boolean fraudDetected,
            ArrayList<String> fraudTypes,
            VerificationResult.ValidationStatus status,
            Map<String, Object> metadata) {
        
        return VerificationResult.builder()
                .userId(userId)
                .verificationType(verificationType)
                .createdAt(LocalDateTime.now())
                .processedAt(LocalDateTime.now())
                .fraudDetected(fraudDetected)
                .fraudTypes(fraudTypes)
                .status(status)
                .metadata(metadata)
                .build();
    }
    
    /**
     * Helper method to create a rejected response without saving to DB
     */
    private VerificationResponse createRejectedResponse(
            String userId,
            VerificationResult.VerificationType verificationType,
            String message) {
        
        return VerificationResponse.builder()
                .userId(userId)
                .verificationType(verificationType)
                .processedAt(LocalDateTime.now())
                .fraudDetected(false)
                .status(VerificationResult.ValidationStatus.REJECTED)
                .message(message)
                .build();
    }
    
    /**
     * Maps a verification result entity to a response DTO
     */
    private VerificationResponse mapToResponse(VerificationResult result) {
        return VerificationResponse.builder()
                .id(result.getId())
                .userId(result.getUserId())
                .verificationType(result.getVerificationType())
                .processedAt(result.getProcessedAt())
                .fraudDetected(result.isFraudDetected())
                .fraudTypes(result.getFraudTypes())
                .status(result.getStatus())
                .message(result.isFraudDetected() ? 
                        "Fraud detected: " + String.join(", ", result.getFraudTypes()) : 
                        "Verification successful")
                .build();
    }
} 