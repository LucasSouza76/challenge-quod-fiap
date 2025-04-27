package com.quod.biometric.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Slf4j
public class ImageValidationService {

    private final long maxFileSize;
    private final List<String> allowedFormats;
    private final String minResolution;

    public ImageValidationService(
            @Value("${image.validation.max-size}") long maxFileSize,
            @Value("${image.validation.min-resolution}") String minResolution) {
        this.maxFileSize = maxFileSize;
        this.minResolution = minResolution;
        this.allowedFormats = Arrays.asList("image/jpeg", "image/png");
    }

    /**
     * Performs basic validation of image files
     * @param image The image file to validate
     * @return Map with validation results
     */
    public Map<String, Object> validateImage(MultipartFile image) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // Check if file is empty
        if (image.isEmpty()) {
            errors.add("File is empty");
            result.put("valid", false);
            result.put("errors", errors);
            return result;
        }
        
        // Check file size
        if (image.getSize() > maxFileSize) {
            errors.add("File size exceeds the maximum allowed size of " + (maxFileSize / 1024 / 1024) + "MB");
        }
        
        // Check file format
        String contentType = image.getContentType();
        if (contentType == null || !allowedFormats.contains(contentType)) {
            errors.add("File format not supported. Allowed formats: " + String.join(", ", allowedFormats));
        }
        
        // Extract metadata (simulated)
        Map<String, Object> metadata = extractMetadata(image);
        result.put("metadata", metadata);
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }
    
    /**
     * Simulates extracting metadata from an image
     * In a real application, this would use libraries like Apache Commons Imaging or metadata-extractor
     */
    private Map<String, Object> extractMetadata(MultipartFile image) {
        Map<String, Object> metadata = new HashMap<>();
        // In a real implementation, extract actual metadata from the image
        metadata.put("filename", image.getOriginalFilename());
        metadata.put("contentType", image.getContentType());
        metadata.put("size", image.getSize());
        metadata.put("captureDate", new Date());
        metadata.put("deviceManufacturer", "Simulated Device Manufacturer");
        
        // For demonstration purposes, generate random metadata
        if (Math.random() > 0.5) {
            metadata.put("gpsLatitude", "40.7128° N");
            metadata.put("gpsLongitude", "74.0060° W");
        }
        
        return metadata;
    }
} 