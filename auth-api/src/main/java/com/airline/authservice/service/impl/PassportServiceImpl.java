package com.airline.authservice.service.impl;

import com.airline.authservice.common.GoogleDriveUploader;
import com.airline.authservice.common.TokenStorage;
import com.airline.authservice.model.User;
import com.airline.authservice.repository.UserRepository;
import com.airline.authservice.service.PassportService;
import com.airline.authservice.service.UserService;
import com.airlines.airlinesharedmodule.MessageDto;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class PassportServiceImpl implements PassportService {

    private static final String APPLICATION_NAME = "AirlineApp";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String redirectUri = "http://localhost:8000/passport/Callback";

    private final UserService userService;
    private final UserRepository userRepository;

    public PassportServiceImpl(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @Override
    public String getOAuthConsentLink(Long userId) throws Exception {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(getClass().getResourceAsStream("/credentials.json"))
        );

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                Collections.singletonList(DriveScopes.DRIVE_FILE)
        )
                .setAccessType("offline")
                .build();

//        String redirectUri = "http://localhost:8888/Callback"; // ili tvoj redirect sa fronta
        String url = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setState(userId.toString())
                .build();
        return url;
    }

    @Override
    public String handleGoogleCallback(String code, String userIdStr) throws Exception {
        Long userId = Long.parseLong(userIdStr); // safely extract userId

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(getClass().getResourceAsStream("/credentials.json"))
        );

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                Collections.singletonList("openid email https://www.googleapis.com/auth/drive.file")
        )
                .setAccessType("offline")
                .build();

        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();

        // ✔ Associate token with userId instead of email
        Credential credential = flow.createAndStoreCredential(tokenResponse, userId.toString());

        // Store using userId
        TokenStorage.save(userId, credential);
        return "Authorization successful for user ID: " + userId;
    }

    @Override
    public MessageDto uploadFile(MultipartFile multipartFile, Long userId) {
        System.out.println("hej");
        try{
            // inicijalizacija servisa
//            Drive service = GoogleDriveUploader.getDriveService();
            // oAuth
//            Drive service = new Drive.Builder(
//                    GoogleNetHttpTransport.newTrustedTransport(),
//                    JSON_FACTORY,
//                    GoogleDriveUploader.getCredentials(GoogleNetHttpTransport.newTrustedTransport()))
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();

//            String email = userService.findUserById(userId).getEmail();
            Credential credential = TokenStorage.get(userId); // Preuzmi token za korisnika

            Drive service = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    credential
            ).setApplicationName(APPLICATION_NAME).build();


            //kreiranje foldera ako user nema vec svoj folder
            String userFolderId = GoogleDriveUploader.findOrCreateUserFolder(service, userId);

            // Postavi roditelja fajla kao userFolderId
            // Cuvanje MultipartFile u privremeni fajl
            java.io.File tempFile = java.io.File.createTempFile("upload-", multipartFile.getOriginalFilename());
            try(OutputStream os = new FileOutputStream(tempFile)){
                os.write(multipartFile.getBytes());
            }

            //metapodaci
            File fileMetaData= new File();
            fileMetaData.setName(multipartFile.getOriginalFilename());
//            fileMetaData.setParents(Collections.singletonList(FOLDER_ID));
            fileMetaData.setParents(Collections.singletonList(userFolderId));

            // Mime type fajla
            String mimeType = multipartFile.getContentType();

            FileContent mediaContent = new FileContent(mimeType,tempFile);

            // upload
            File uploadedFile = service.files().create(fileMetaData, mediaContent)
                    .setFields("id")
                    .setSupportsAllDrives(true)
                    .execute();

            tempFile.delete();


            User user= userService.findUserById(userId);
            user.setPassportId(fileMetaData.getName());
            userRepository.save(user);

            return MessageDto.builder().message("File uploaded successfull with ID: "+ uploadedFile.getId()).code("200").build();
        } catch (GeneralSecurityException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(Long userId, String fileName) {
        try {
            // Inicijalizuj Drive servis za korisnika
//            Drive service = new Drive.Builder(
//                    GoogleNetHttpTransport.newTrustedTransport(),
//                    JSON_FACTORY,
//                    GoogleDriveUploader.getCredentials(GoogleNetHttpTransport.newTrustedTransport()))// koristi userId za dohvat korisničkih kredencijala
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
            Credential credential = TokenStorage.get(userId); // Preuzmi token za korisnika

            Drive service = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    credential
            ).setApplicationName(APPLICATION_NAME).build();


            // Pronađi folder korisnika
            String folderId = GoogleDriveUploader.findOrCreateUserFolder(service, userId);
            if (folderId == null) {
                return ResponseEntity.notFound().build();
            }

            // Pronađi fajl u folderu
            String query = "mimeType!='application/vnd.google-apps.folder' and '" + folderId + "' in parents and name='" + fileName + "' and trashed=false";
            Drive.Files.List request = service.files().list().setQ(query).setSpaces("drive").setFields("files(id, name)");
            List<File> files = request.execute().getFiles();

            if (files == null || files.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            File file = files.get(0);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                    .body(outputStream.toByteArray());

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}