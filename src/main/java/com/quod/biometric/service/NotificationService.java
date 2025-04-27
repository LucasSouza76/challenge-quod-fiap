package com.quod.biometric.service;

import com.quod.biometric.model.VerificationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate;
    
    @Value("${notification.service.url}")
    private String fraudNotificationUrl;
    
    @Value("${notification.service.success-url}")
    private String successNotificationUrl;

    /**
     * Sends a fraud notification to the external system
     * @param verificationResult The verification result containing fraud details
     * @return Notification ID
     */
    public String sendFraudNotification(VerificationResult verificationResult) {
        String notificationId = UUID.randomUUID().toString();
        log.info("Sending fraud notification with ID: {}", notificationId);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("notificationId", notificationId);
            requestBody.put("verificationId", verificationResult.getId());
            requestBody.put("userId", verificationResult.getUserId());
            requestBody.put("verificationType", verificationResult.getVerificationType());
            requestBody.put("fraudTypes", verificationResult.getFraudTypes());
            requestBody.put("timestamp", verificationResult.getProcessedAt());
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // Since this is a simulation, log the request instead of making an actual call
            log.info("Simulating HTTP POST to {} with payload: {}", fraudNotificationUrl, requestBody);
            
            // In a real implementation, this would make an actual HTTP call
            // ResponseEntity<String> response = restTemplate.postForEntity(fraudNotificationUrl, request, String.class);
            
            return notificationId;
        } catch (Exception e) {
            log.error("Error sending fraud notification", e);
            return null;
        }
    }
    
    /**
     * Sends a success notification to the external system
     * @param verificationResult The successful verification result
     * @return Notification ID
     */
    public String sendSuccessNotification(VerificationResult verificationResult) {
        String notificationId = UUID.randomUUID().toString();
        log.info("Sending success notification with ID: {}", notificationId);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("notificationId", notificationId);
            requestBody.put("verificationId", verificationResult.getId());
            requestBody.put("userId", verificationResult.getUserId());
            requestBody.put("verificationType", verificationResult.getVerificationType());
            requestBody.put("timestamp", verificationResult.getProcessedAt());
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // Since this is a simulation, log the request instead of making an actual call
            log.info("Simulating HTTP POST to {} with payload: {}", successNotificationUrl, requestBody);
            
            // In a real implementation, this would make an actual HTTP call
            // ResponseEntity<String> response = restTemplate.postForEntity(successNotificationUrl, request, String.class);
            
            return notificationId;
        } catch (Exception e) {
            log.error("Error sending success notification", e);
            return null;
        }
    }
} 