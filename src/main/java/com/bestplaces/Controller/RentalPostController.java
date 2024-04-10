package com.bestplaces.Controller;

import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.Impl.UserServiceImpl;
import com.bestplaces.Service.RentalPostService;
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
@RequestMapping("/post")
public class RentalPostController {
    @Autowired
    private RentalPostService rentalPostService;

    @Autowired
    private TestUploadService service;

    @Autowired UserRepository userRepository;

    @GetMapping()
    public String showAddRentalPostForm(Model model) {
        model.addAttribute("rentalPost", new RentalPost());
        return "CreatePost";
    }

    @PostMapping
    public String createRentalPost(@ModelAttribute("rentalpost") RentalPostDto rentalPostDto) {
        rentalPostService.saveRentalPost(rentalPostDto);
        return "redirect:/save?success";
    }

    @ResponseBody
    @PostMapping("/post")
    public Object handleFileUpload(@RequestParam("image") MultipartFile file) throws IOException, GeneralSecurityException {
        if (file.isEmpty()) {
            return "File is empty";
        }
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        Res res = service.uploadImageToDrive(tempFile);
        System.out.println(res);
        return res;
    }
}


