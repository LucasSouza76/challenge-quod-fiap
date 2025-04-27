package com.quod.biometric.controller;

import com.quod.biometric.dto.DocumentAnalysisRequest;
import com.quod.biometric.dto.FacialBiometryRequest;
import com.quod.biometric.dto.FingerprintBiometryRequest;
import com.quod.biometric.dto.VerificationResponse;
import com.quod.biometric.service.BiometricVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/verification")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "QUOD Document Verification API", description = "API endpoints for document and identity verification")
public class DocumentVerificationController {

    private final BiometricVerificationService biometricVerificationService;

    @PostMapping(value = "/facial", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Process facial biometry verification", 
               description = "Validates a facial image and checks for fraud patterns")
    public ResponseEntity<VerificationResponse> processFacialBiometry(
            @ModelAttribute @Valid FacialBiometryRequest request) {
        
        log.info("Received facial biometry verification request for user ID: {}", request.getUserId());
        VerificationResponse response = biometricVerificationService.processFacialBiometry(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/fingerprint", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Process fingerprint biometry verification", 
               description = "Validates a fingerprint image and checks for fraud patterns")
    public ResponseEntity<VerificationResponse> processFingerprintBiometry(
            @ModelAttribute @Valid FingerprintBiometryRequest request) {
        
        log.info("Received fingerprint biometry verification request for user ID: {}", request.getUserId());
        VerificationResponse response = biometricVerificationService.processFingerprintBiometry(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Process document analysis verification", 
               description = "Validates document and face images, comparing them and checking for fraud patterns")
    public ResponseEntity<VerificationResponse> processDocumentAnalysis(
            @ModelAttribute @Valid DocumentAnalysisRequest request) {
        
        log.info("Received document analysis verification request for user ID: {}", request.getUserId());
        VerificationResponse response = biometricVerificationService.processDocumentAnalysis(request);
        return ResponseEntity.ok(response);
    }
} 