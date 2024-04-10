package com.bestplaces.Controller;

import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.FilterSearchService;
import com.bestplaces.Service.MyUserDetailsService;
import com.bestplaces.Service.RentalPostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
//    private User user;
    @Autowired
    private RentalPostService rentalPostService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private FilterSearchService filterSearch;

    @GetMapping("/home")
    public String Home(HttpServletRequest request, Model model) {

        // Lấy thông tin người dùng từ cookies
        Cookie[] cookies = request.getCookies();
        String username = null;
        String avatarUrl = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                } else if (cookie.getName().equals("avatarUrl")) {
                    avatarUrl = cookie.getValue();
                }
            }
        }

        // Thêm thông tin người dùng vào model để sử dụng trong template
        model.addAttribute("username", username);
        model.addAttribute("avatarUrl", avatarUrl);
        model.addAttribute("showSearchResults", false);
        List<RentalPost> posts = rentalPostService.getAllPosts();
        model.addAttribute("posts", posts);
        return "home";
    }

@PostMapping("/home")
public String filterSearch(@RequestParam(required = false, value = "priceRange") String priceRange,
                           @RequestParam(required = false, value = "areaRange") String areaRange,
                           @RequestParam(required = false, value = "Type") String type,
                           Model model) {
    Double minPrice = null;
    Double maxPrice = null;
    Integer minArea = null;
    Integer maxArea = null;
//    String type = null;

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

    List<RentalPost> rentalPosts = filterSearch.searchPost(minPrice, maxPrice, minArea, maxArea, type);
    model.addAttribute("rentalPosts", rentalPosts);
    model.addAttribute("showSearchResults", true);
    return "home";
}

}
