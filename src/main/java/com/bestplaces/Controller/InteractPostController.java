package com.bestplaces.Controller;

import com.bestplaces.Service.RentalPostService;
import com.bestplaces.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InteractPostController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private RentalPostService rentalPostService;

    @PostMapping("/report")
    public String reportPost(@RequestParam("postId") String postId, @RequestParam("reason") String reason){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        reportService.reportPost(postId, username, reason);
        return String.format("redirect:/detail-post/%s", postId);
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
