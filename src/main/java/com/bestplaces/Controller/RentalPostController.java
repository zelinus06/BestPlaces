package com.bestplaces.Controller;

import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.RentalPost;
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

    @Autowired UserRepository userRepository;

    @Autowired
    private UploadService uploadService;

    @GetMapping()
    public String showAddRentalPostForm(Model model) {
        model.addAttribute("rentalPost", new RentalPost());
        return "CreatePost";
    }

    @PostMapping()
    public String createRentalPost(@ModelAttribute("rentalpost") RentalPostDto rentalPostDto){
        rentalPostService.saveRentalPost(rentalPostDto);
        return "CreatePost";
    }
}


