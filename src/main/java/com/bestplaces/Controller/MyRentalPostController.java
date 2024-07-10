package com.bestplaces.Controller;

import com.bestplaces.Dto.PostDto;
import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.RentalPostService;
import com.bestplaces.Service.UploadService;
import com.bestplaces.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@RequestMapping("/post")
public class MyRentalPostController {
    @Autowired
    private RentalPostService rentalPostService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UploadService service;
    @Autowired
    private UsersService usersService;

    @GetMapping()
    public String showAddRentalPostForm(Model model) {
        User currentUser = usersService.getCurrentUser();
        System.out.println("phonenumber" + currentUser.getPhoneNumber());
        if (currentUser.getPhoneNumber().isEmpty()) {
            return "redirect:/addPhoneNumber";
        } else {
            model.addAttribute("rentalPost", new RentalPost());
            model.addAttribute("currentUser", currentUser);
            return "CreatePost";
        }
    }

    @PostMapping()
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
        return "redirect:/post";
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

    @GetMapping("/mypost")
    public String showUserPost(Model model) {
        List<RentalPost> list = rentalPostService.getUserPost();
        List<PostDto> posts = rentalPostService.getPosts(list);
        User currentUser = usersService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", posts);
        return "UserPost";
    }

    @PostMapping("/update")
    public String UpdatePost(@RequestParam(value = "idpost") Long idpost,
                             @RequestParam(required = false, value = "newArea") int newArea,
                             @RequestParam(required = false, value = "newPrice") int newPrice,
                             @RequestParam(required = false, value = "newCity") String newCity,
                             @RequestParam(required = false, value = "newDistrict") String newDistrict,
                             @RequestParam (required = false, value = "newCommune") String newCommune,
                             @RequestParam (required = false, value = "newStreet") String newStreet,
                             @RequestParam (required = false, value = "newTitle") String newTitle,
                             @RequestParam (required = false, value = "newNumberHouse") String newNumberHouse,
                             @RequestParam (required = false, value = "newDescription") String newDescription,
                             @RequestParam (required = false, value = "newType") Type newType,
                                                                                 Model model) {
        User currentUser = usersService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        rentalPostService.updatePost(idpost, newArea, newPrice, newCity, newDistrict, newCommune, newStreet, newNumberHouse, newDescription, newTitle, newType);
        return "redirect:/post/mypost";
    }

    @DeleteMapping("/delete/{idpost}")
    public String DeletePost(@PathVariable("idpost") Long postId) {
        rentalPostService.deleteAllComment(postId);
        rentalPostService.deletePost(postId);
        return "redirect:/post/mypost";
    }
}


