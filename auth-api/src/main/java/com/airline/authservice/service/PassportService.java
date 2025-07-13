package com.airline.authservice.service;

import com.airline.authservice.dto.MessageDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PassportService {
    String getOAuthConsentLink(Long userId) throws Exception;
    String handleGoogleCallback(String code, String userIdStr) throws Exception;

     MessageDto uploadFile(MultipartFile multipartFile, Long userId);

    ResponseEntity<byte[]> downloadFile(Long userId, String fileName);
     }