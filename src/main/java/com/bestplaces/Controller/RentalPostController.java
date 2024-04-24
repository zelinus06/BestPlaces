package com.bestplaces.Controller;

import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.RentalPostService;
import com.bestplaces.Service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/post")
public class RentalPostController {
    @Autowired
    private RentalPostService rentalPostService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UploadService service;

    @Autowired
    private PostRepository postRepository;

    @GetMapping()
    public String showAddRentalPostForm(Model model) {
        model.addAttribute("rentalPost", new RentalPost());
        return "CreatePost";
    }

    @PostMapping("")
    public String createRentalPost(@ModelAttribute("rentalpost") RentalPostDto rentalPostDto, @RequestParam("files")  MultipartFile[] files)throws IOException, GeneralSecurityException {
        RentalPost rentalPost = rentalPostService.saveRentalPost(rentalPostDto);
        for (MultipartFile file : files) {
            try {
                File tempFile = File.createTempFile("temp", null);
                file.transferTo(tempFile);
                Res res = service.uploadImageToDrive(tempFile);
                service.saveImagePathForUser(res.getUrl(), rentalPost);
            } catch (IOException e) {
                e.printStackTrace();
                return "Error occurred while processing file: " + e.getMessage();

            }
        }
        return "CreatePost";
    }

    @ResponseBody
    @PostMapping("/uploadToGoogleDrive")
    public Object handleFileUpload(@RequestParam("image") MultipartFile file,  @RequestParam("postId") RentalPost postId) throws IOException, GeneralSecurityException {
        if (file.isEmpty()) {
            return "File is empty";
        }
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        Res res = service.uploadImageToDrive(tempFile);
        return res;
    }
}


