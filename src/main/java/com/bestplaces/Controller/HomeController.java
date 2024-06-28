package com.bestplaces.Controller;

import com.bestplaces.Dto.PostDto;
import com.bestplaces.Dto.SearchResDto;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.FilterSearchService;
import com.bestplaces.Service.MyUserDetailsService;
import com.bestplaces.Service.RecommenderService;
import com.bestplaces.Service.RentalPostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private RentalPostService rentalPostService;
    @Autowired
    private FilterSearchService filterSearch;
    @Autowired
    private RecommenderService recommenderService;

    @GetMapping("/")
    public String Home(Model model, @CookieValue("username")String username, @CookieValue("avatarUrl")String avatarUrl,
                       @RequestParam(name = "page", defaultValue = "1") int page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<RentalPost> postPage = rentalPostService.getAllPosts(pageable);
        // Chuyển đổi danh sách bài đăng sang đối tượng DTO để hiển thị trên view
        List<PostDto> posts = rentalPostService.getPosts(postPage.getContent());
        // Đưa danh sách bài đăng và thông tin phân trang vào model
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("username", username);
        model.addAttribute("avatarUrl", avatarUrl);
        model.addAttribute("showSearchResults", false);
        return "home";
    }

    @PostMapping()
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
        if (!"0".equals(priceRange)) {
                    String[] priceParts = priceRange.split("-");
                    minPrice = Double.parseDouble(priceParts[0]);
                    maxPrice = Double.parseDouble(priceParts[1]);
        }
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

            if (!"0".equals(areaRange)) {
                String[] areaParts = areaRange.split("-");
                minArea = Integer.parseInt(areaParts[0]);
                maxArea = Integer.parseInt(areaParts[1]);
            }
        List<RentalPost> rentalPosts = filterSearch.searchPost(minPrice, maxPrice, minArea, maxArea, type, city, district, commune);
        List<PostDto> postDTOs = rentalPostService.getPosts(rentalPosts);
        model.addAttribute("rentalPosts", postDTOs);
        model.addAttribute("showSearchResults", true);
        SearchResDto searchResDto = new SearchResDto();
        searchResDto.setType(type);
        searchResDto.setCity(city);
        searchResDto.setCommune(commune);
        searchResDto.setDistrict(district);
        searchResDto.setPriceRange(priceRange);
        searchResDto.setAreaRange(areaRange);
        model.addAttribute("searched", searchResDto);
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

    @PostMapping("/type")
    public String findBedsit(Model model, @RequestParam("value") String type) {
        Double minPrice = null, maxPrice = null;
        Integer minArea = null, maxArea = null;
        String city = null, district = null, commune = null;
        List<RentalPost> rentalPosts = filterSearch.searchPost(minPrice, maxPrice, minArea, maxArea, type, city, district, commune);
        List<PostDto> postDTOs = rentalPostService.getPosts(rentalPosts);
        model.addAttribute("rentalPosts", postDTOs);
        model.addAttribute("showSearchResults", true);
        return "home";
    }
}
