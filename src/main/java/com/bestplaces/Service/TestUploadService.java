package com.bestplaces.Service;

import com.bestplaces.Dto.Res;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class TestUploadService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoodleCredentials();

    private static String getPathToGoodleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "bestplaces-418608-bbcbdbf8e162.json");
        return filePath.toString();
    }

    public Res uploadImageToDrive(File file) throws GeneralSecurityException, IOException {
        Res res = new Res();

        try{
            String folderId = "1mithqebMP1qEoDg7cE_sU1rpKS7_TcRv";
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/png", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent)
                    .setFields("id").execute();
            String imageUrl = "https://drive.google.com/uc?export=view&id="+uploadedFile.getId();
            System.out.println("IMAGE URL: " + imageUrl);
            file.delete();
            res.setStatus(200);
            res.setMessage("Image Successfully Uploaded To Drive");
            res.setUrl(imageUrl);
        }catch (Exception e){
            System.out.println(e.getMessage());
            res.setStatus(501);
            res.setMessage(e.getMessage());
        }
        return  res;
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .build();
    }
}
