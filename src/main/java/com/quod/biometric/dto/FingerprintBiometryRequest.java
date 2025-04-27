package com.quod.biometric.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FingerprintBiometryRequest {

    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Fingerprint image is required")
    private MultipartFile fingerprintImage;
    
    @NotBlank(message = "Finger position is required")
    private String fingerPosition; // e.g., "RIGHT_INDEX", "LEFT_THUMB"
    
    private String deviceInfo;
    private String geoLocation;
} 