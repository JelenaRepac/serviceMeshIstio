package com.airline.authservice.controller;

import com.airline.authservice.dto.MessageDto;
import com.airline.authservice.service.PassportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/passport")
public class PassportController {


    private final PassportService passportService;
    public PassportController( PassportService passportService) {
        this.passportService = passportService;
    }

    @GetMapping("/oauth/link")
    public ResponseEntity<String> getOAuthConsentLink(@RequestParam("userId") Long userId) throws Exception {
        String url =passportService.getOAuthConsentLink(userId);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/Callback")
    public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String code, @RequestParam("state") String userIdStr) throws Exception {

        return ResponseEntity.ok(passportService.handleGoogleCallback(code, userIdStr));
    }


    @PostMapping("/upload")
    public ResponseEntity<MessageDto> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("id") Long userId) {
        MessageDto messageDto= passportService.uploadFile(multipartFile, userId);
        return new ResponseEntity<>(messageDto, HttpStatus.OK);

    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam Long userId, @RequestParam String fileName) {
        return passportService.downloadFile(userId, fileName);
    }


}
