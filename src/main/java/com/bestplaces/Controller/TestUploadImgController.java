package com.bestplaces.Controller;

import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Service.TestUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
public class TestUploadImgController {
    @Autowired
    private TestUploadService service;

    @GetMapping("/TestUploadImg")
    public String showVerifyCodeForm(Model model) {
        model.addAttribute("user", new User());
        return "TestUploadImg";
    }

    @ResponseBody
    @PostMapping("/uploadToGoogleDrive")
    public Object handleFileUpload(@RequestParam("image") MultipartFile file) throws IOException, GeneralSecurityException {
        if (file.isEmpty()) {
            return "FIle is empty";
        }
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        Res res = service.uploadImageToDrive(tempFile);
        System.out.println(res);
        return res;
    }
}
