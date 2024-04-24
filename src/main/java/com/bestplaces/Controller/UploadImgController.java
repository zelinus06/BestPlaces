package com.bestplaces.Controller;

import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UploadImgController {
    @Autowired
    private UploadService service;

    @GetMapping("/TestUploadImg")
    public String showVerifyCodeForm(Model model) {
        model.addAttribute("user", new User());
        return "TestUploadImg";
    }

//    @ResponseBody
//    @PostMapping("/uploadToGoogleDrive")
//    public Object handleFileUpload(@RequestParam("image") MultipartFile file,  @RequestParam("postId") RentalPost postId) throws IOException, GeneralSecurityException {
//        if (file.isEmpty()) {
//            return "File is empty";
//        }
//        File tempFile = File.createTempFile("temp", null);
//        file.transferTo(tempFile);
//        Res res = service.uploadImageToDrive(tempFile);
//
//        service.saveImagePathForUser(res.getUrl(), postId);
//        return res;
//    }
}
