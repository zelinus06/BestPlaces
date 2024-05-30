package com.bestplaces.Controller;

import com.bestplaces.Dto.CommentDto;
import com.bestplaces.Dto.PostDto;
import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Dto.Res;
import com.bestplaces.Entity.Comment;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.RentalPostService;
import com.bestplaces.Service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/post")
public class RentalPostController {
    @Autowired
    private RentalPostService rentalPostService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UploadService service;

    @GetMapping()
    public String showAddRentalPostForm(Model model) {
        model.addAttribute("rentalPost", new RentalPost());
        return "CreatePost";
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
                             @RequestParam (required = false, value = "newType") Type newType) {
        rentalPostService.updatePost(idpost, newArea, newPrice, newCity, newDistrict, newCommune, newStreet, newNumberHouse, newDescription, newTitle, newType);
        return "UserPost";
    }

    @DeleteMapping("/delete/{idpost}")
    public String DeletePost(@PathVariable("idpost") Long postId) {
        rentalPostService.deletePost(postId);
        return "redirect:/user/post";
    }

    @PostMapping("/comment")
    public String RatePost(@RequestParam("idpost") long postId,
                           @RequestParam(value = "Comment") String comment) {
        rentalPostService.comment(postId, comment);
        return String.format("redirect:/post/%d", postId);
    }

    @GetMapping("/{postId}")
    public String getPostDetail(@PathVariable Long postId, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Optional<User> users = userRepository.findByUsername(username);
        model.addAttribute("currentUser", users.get());

        PostDto postDto = rentalPostService.getDetailPost(postId);
        List<CommentDto> comments = rentalPostService.showComment(postId);
        model.addAttribute("commentDto", comments);
        model.addAttribute("postDto", postDto);
        return "ChosenPost";
    }

    @DeleteMapping("/deleteComment/{postId}/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, @PathVariable("postId") Long postId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Optional<User> users = userRepository.findByUsername(username);
        Comment comment = rentalPostService.getCommentById(commentId);
        if(Objects.equals(comment.getId_user().getId(), users.get().getId())) {
            rentalPostService.deleteComment(commentId);
            return String.format("redirect:/post/%d", postId);
        } else {
            return String.format("redirect:/post/%d", postId);
        }
    }
}


