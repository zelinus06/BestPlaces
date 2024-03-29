////package com.bestplaces.Controller;
////
////
////import com.bestplaces.Dto.Res;
////import com.bestplaces.Service.TestUploadService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Controller;
////import org.springframework.web.bind.annotation.PostMapping;
//import com.bestplaces.Dto.Res;
//import com.bestplaces.Service.TestUploadService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//
//@Controller
//public class TestUploadController {
//
//    @Autowired
//    private TestUploadService service;
//
//    @PostMapping("/uploadToGoogleDrive")
//    public Object handleFileUpload(@RequestParam("image") MultipartFile file) throws IOException, GeneralSecurityException {
//        if (file.isEmpty()) {
//            return "FIle is empty";
//        }
//        File tempFile = File.createTempFile("temp", null);
//        file.transferTo(tempFile);
//        Res res = service.uploadImageToDrive(tempFile);
//        System.out.println(res);
//        return res;
//    }
//}