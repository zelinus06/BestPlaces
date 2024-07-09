package com.bestplaces.Controller;

import com.bestplaces.Dto.PostDto;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Service.RecommenderService;
import com.bestplaces.Service.RentalPostService;
import com.bestplaces.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/recommend")
public class RecommendController {
    @Autowired
    private RecommenderService recommenderService;
    @Autowired
    private RentalPostService rentalPostService;
    @Autowired
    private UsersService usersService;

    @PostMapping()
    public String recommendHouse(Model model) {
        List<RentalPost> rentalPosts = recommenderService.recommend();
        if (rentalPosts == null) {
            model.addAttribute("showSearchResults", false);
            User currentUser = usersService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
            return "Recommend";
        } else {
            List<PostDto> postDTOs = rentalPostService.getPosts(rentalPosts);
            model.addAttribute("rentalPosts", postDTOs);
            model.addAttribute("showSearchResults", true);
            User currentUser = usersService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
            return "Recommend";
        }
    }
}
