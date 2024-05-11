package com.bestplaces.Controller;

import com.bestplaces.Dto.PostDto;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.FilterSearchService;
import com.bestplaces.Service.MyUserDetailsService;
import com.bestplaces.Service.RecommenderService;
import com.bestplaces.Service.RentalPostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {
    @Autowired
    private RentalPostService rentalPostService;
    @Autowired
    private FilterSearchService filterSearch;
    @Autowired
    private RecommenderService recommenderService;

    @GetMapping("/home")
    public String Home(Model model, @CookieValue("username")String username, @CookieValue("avatarUrl")String avatarUrl) {
        model.addAttribute("username", username);
        model.addAttribute("avatarUrl", avatarUrl);
        model.addAttribute("showSearchResults", false);
        List<RentalPost> list = rentalPostService.getAllPosts();
        List<PostDto> posts = rentalPostService.getPosts(list);
        model.addAttribute("posts", posts);
        return "home";
    }

    @PostMapping("/home")
    public String filterSearch(@RequestParam(required = false, value = "priceRange") String priceRange,
                               @RequestParam(required = false, value = "areaRange") String areaRange,
                               @RequestParam(required = false, value = "Type") String type,
                               @RequestParam(required = false, value = "city") String city,
                               @RequestParam(required = false, value = "district") String district,
                               @RequestParam(required = false, value = "commune") String commune,
                               Model model) {
        Double minPrice = null;
        Double maxPrice = null;
        Integer minArea = null;
        Integer maxArea = null;
        if (Objects.equals(type, "")) {
            type = null;
        }
        if (Objects.equals(city, "")) {
            city = null;
        }
        if (Objects.equals(district, "")) {
            district = null;
        }
        if (Objects.equals(commune, "")) {
            commune = null;
        }
        if (!"0".equals(priceRange)) {
            String[] priceParts = priceRange.split("-");
            minPrice = Double.parseDouble(priceParts[0]);
            maxPrice = Double.parseDouble(priceParts[1]);
        }

        if (!"0".equals(areaRange)) {
            String[] areaParts = areaRange.split("-");
            minArea = Integer.parseInt(areaParts[0]);
            maxArea = Integer.parseInt(areaParts[1]);
        }
        List<RentalPost> rentalPosts = filterSearch.searchPost(minPrice, maxPrice, minArea, maxArea, type, city, district, commune);
        List<PostDto> postDTOs = rentalPostService.getPosts(rentalPosts);
        model.addAttribute("rentalPosts", postDTOs);
        model.addAttribute("showSearchResults", true);
        return "home";
    }

    @PostMapping("/recommend")
    public String recommendHouse(Model model) {
        List<RentalPost> rentalPosts = recommenderService.recommend();
        List<PostDto> postDTOs = rentalPostService.getPosts(rentalPosts);
        model.addAttribute("rentalPosts", postDTOs);
        model.addAttribute("showSearchResults", true);
        return "home";
    }
}
