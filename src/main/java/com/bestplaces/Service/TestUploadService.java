package com.bestplaces.Service;

import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Service.Impl.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.hibernate.query.spi.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class TestUploadService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoodleCredentials();

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PostRepository postRepository;

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

            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileNameWithTimestamp ="_" + timestamp + "_" + file.getName();

            fileMetaData.setName(fileNameWithTimestamp);
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/png", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent)
                    .setFields("id").execute();
            String imageUrl = "https://drive.google.com/thumbnail?id="+uploadedFile.getId();
            saveImagePathForUser(imageUrl);
            System.out.println("IMAGE URL: " + imageUrl);
            file.delete();
            res.setStatus(200);
            res.setMessage("Image Successfully Uploaded To Drive");
            res.setUrl(imageUrl);
//            saveImagePathForUser(imageUrl,userId);
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

    public void saveImagePathForUser(String imagePath) {
        RentalPost rentalPostOptional = postRepository.findByUserId(userService.getUserIdByUsernames());
        if (rentalPostOptional != null) {
            rentalPostOptional.setImagePath(imagePath);
            postRepository.save(rentalPostOptional);
        } else {
            // Xử lý khi không tìm thấy bài đăng thuê tương ứng với người dùng
            System.out.println("Không tìm thấy bài đăng thuê tương ứng với người dùng");
        }
    }
}
