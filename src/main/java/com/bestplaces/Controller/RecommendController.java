package com.bestplaces.Controller;

import com.bestplaces.Dto.PostDto;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Service.FindExpectedLocation;
import com.bestplaces.Service.RecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendController {
    @Autowired
    private FindExpectedLocation findExpectedResult;
    @Autowired
    private RecommenderService recommenderService;
    @GetMapping("/rs")
    public String test() {
        return findExpectedResult.FindExResult();
    }
    @GetMapping("/recommend")
    public void showResult(Model model) {
//        List<PostDto> postDtos = recommenderService.recommend();

    }


}
