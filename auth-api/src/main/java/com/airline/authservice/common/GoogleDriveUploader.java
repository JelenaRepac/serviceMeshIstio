package com.airline.authservice.common;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleDriveUploader {

    private static final String APPLICATION_NAME = "YourAppName";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";// Path in resources
    private static final String CREDENTIALS_SERVICE_ACCOUNT_FILE_PATH = "/stable-smithy-458908-p2-729d4bd0ee31.json";// Path in resources




    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        InputStream in = GoogleDriveUploader.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new IOException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive"));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public static String getAuthorizationUrl() throws IOException {
        InputStream in = GoogleDriveUploader.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new IOException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        String clientId = clientSecrets.getDetails().getClientId();
        String redirectUri = "http://localhost:8888/Callback"; // your redirect URI

        GoogleAuthorizationCodeRequestUrl url = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                redirectUri,
                SCOPES
        ).setAccessType("offline");


        return url.build();
    }
    public static Credential getServiceAccountCredentials() throws IOException, GeneralSecurityException {
        InputStream in = GoogleDriveUploader.class.getResourceAsStream(CREDENTIALS_SERVICE_ACCOUNT_FILE_PATH);

        GoogleCredential credential = GoogleCredential
                .fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return credential;
    }

    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleDriveUploader.class.getResourceAsStream("/credentials.json"); // Your OAuth client secret
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Token directory
        File tokenDir = new File("tokens");
        if (!tokenDir.exists()) tokenDir.mkdirs();

        // Set up flow with file-based token storage
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singletonList(DriveScopes.DRIVE_FILE))
                .setDataStoreFactory(new FileDataStoreFactory(tokenDir))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static String findOrCreateUserFolder(Drive service, Long userId) throws IOException {
        String query = "mimeType='application/vnd.google-apps.folder' and name='" + userId + "' and trashed=false";
        Drive.Files.List request = service.files().list().setQ(query).setSpaces("drive");
        List<com.google.api.services.drive.model.File> files = request.execute().getFiles();

        if (files != null && !files.isEmpty()) {
            return files.get(0).getId();
        } else {
            com.google.api.services.drive.model.File folderMetadata = new com.google.api.services.drive.model.File();
            folderMetadata.setName(String.valueOf(userId));
            folderMetadata.setMimeType("application/vnd.google-apps.folder");

            com.google.api.services.drive.model.File folder = service.files().create(folderMetadata).setFields("id").execute();
            return folder.getId();
        }
    }




}
