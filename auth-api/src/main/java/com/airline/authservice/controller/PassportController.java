package com.airline.authservice.controller;

import com.airline.authservice.common.GoogleDriveUploader;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static com.airline.authservice.common.GoogleDriveUploader.getDriveService;

@RestController
@RequestMapping("/passport")
public class PassportController {

    private static final String APPLICATION_NAME = "AirlineApp";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String FOLDER_ID = "1w1MBlepnX6m9uU3euO6RgDu1ntpH11w1";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("id") String userId) {
        System.out.println("hej");
        try{
            // inicijalizacija servisa
//            Drive service = GoogleDriveUploader.getDriveService();

            Drive service = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    GoogleDriveUploader.getCredentials(GoogleNetHttpTransport.newTrustedTransport()))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

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

            return ResponseEntity.ok("File uploaded successfull with ID: "+ uploadedFile.getId());

        } catch (GeneralSecurityException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String userId, @RequestParam String fileName) {
        try {
            // Inicijalizuj Drive servis za korisnika
            Drive service = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    GoogleDriveUploader.getCredentials(GoogleNetHttpTransport.newTrustedTransport()))// koristi userId za dohvat korisničkih kredencijala
                    .setApplicationName(APPLICATION_NAME)
                    .build();

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
