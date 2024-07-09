package com.bestplaces.Controller;

import com.bestplaces.Dto.CommentDto;
import com.bestplaces.Dto.PostDto;
import com.bestplaces.Entity.Comment;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/detail-post")
public class PostController {
    @Autowired
    private RentalPostService rentalPostService;
    @Autowired
    private UsersService usersService;

    @GetMapping("/{postId}")
    public String getPostDetail(@PathVariable Long postId, Model model) {
        User user = usersService.getCurrentUser();
        model.addAttribute("currentUser", user);
        PostDto postDto = rentalPostService.getDetailPost(postId);
        List<CommentDto> comments = rentalPostService.showComment(postId);
        model.addAttribute("commentDto", comments);
        model.addAttribute("postDto", postDto);
        return "ChosenPost";
    }
}
