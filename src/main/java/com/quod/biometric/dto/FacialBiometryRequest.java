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
public class FacialBiometryRequest {

    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Face image is required")
    private MultipartFile faceImage;
    
    private String deviceInfo;
    private String geoLocation;
} 