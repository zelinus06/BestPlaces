package com.bestplaces.Controller;

import com.bestplaces.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping()
    public String reportPost(@RequestParam("postId") String postId, @RequestParam("reason") String reason) {
        reportService.reportPost(postId, reason);
        return String.format("redirect:/detail-post/%s", postId);
    }
}
