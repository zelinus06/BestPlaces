package com.bestplaces.Controller;

import com.bestplaces.Dto.CommentDto;
import com.bestplaces.Dto.PostDto;
import com.bestplaces.Entity.Comment;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.FilterSearchService;
import com.bestplaces.Service.RecommenderService;
import com.bestplaces.Service.RentalPostService;
import com.bestplaces.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserRepository userRepository;

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
    @PostMapping("/comment")
    public String RatePost(@RequestParam("idpost") long postId,
                           @RequestParam(value = "Comment") String comment) {
        rentalPostService.comment(postId, comment);
        return String.format("redirect:/detail-post/%d", postId);
    }

    @DeleteMapping("/deleteComment/{postId}/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, @PathVariable("postId") Long postId) {
            rentalPostService.deleteComment(commentId);
            return String.format("redirect:/detail-post/%d", postId);
    }
}
