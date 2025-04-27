package com.quod.biometric.repository;

import com.quod.biometric.model.VerificationResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationRepository extends MongoRepository<VerificationResult, String> {
    
    List<VerificationResult> findByUserId(String userId);
    
    List<VerificationResult> findByUserIdAndVerificationType(String userId, VerificationResult.VerificationType verificationType);
    
    List<VerificationResult> findByFraudDetected(boolean fraudDetected);
} 