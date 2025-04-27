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
public class DocumentAnalysisRequest {

    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Document image is required")
    private MultipartFile documentImage;
    
    @NotNull(message = "Face image is required")
    private MultipartFile faceImage;
    
    @NotBlank(message = "Document type is required")
    private String documentType; // e.g., "ID_CARD", "PASSPORT", "DRIVER_LICENSE"
    
    private String deviceInfo;
    private String geoLocation;
} 