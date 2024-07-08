package com.bestplaces.Controller;

import com.bestplaces.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UsersService usersService;
    @GetMapping
    public String showUserInfo(Model model) {
        model.addAttribute("currentUser", usersService.getCurrentUser());
        return "UserInfo";
    }
}
