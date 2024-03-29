package com.bestplaces.Controller;

import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.RentalPostService;
import com.bestplaces.Service.TestUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("/post")
public class RentalPostController {

    private RentalPostService rentalPostService;

    @Autowired
    private PostRepository postRepository;

    @GetMapping()
    public String showAddRentalPostForm(Model model) {
        model.addAttribute("rentalPost", new RentalPost());
        return "CreatePost";
    }

    @PostMapping("/posts/create")
    public String createRentalPost(RentalPost rentalPost) {
        // Gọi Service để lưu rentalPost vào cơ sở dữ liệu
        return "redirect:/";
    }
}


