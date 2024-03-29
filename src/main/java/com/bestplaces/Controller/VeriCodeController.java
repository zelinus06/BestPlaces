package com.bestplaces.Controller;

import com.bestplaces.Dto.UserRegistrationDto;
import com.bestplaces.Entity.User;
import com.bestplaces.Service.MyTelegramBot;
import com.bestplaces.Service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VeriCodeController {
    @Autowired
    private MyTelegramBot myTelegramBot;
    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/testVeriCode")
    public String showVerifyCodeForm(Model model) {
        model.addAttribute("user", new User());
        return "testVeriCode";
    }

    @PostMapping("/generate-code")
    public String generateCode(@ModelAttribute("user") UserRegistrationDto userRegistrationDto, Model model) {
        myTelegramBot.sendVerificationCode("6196949391");
        return "testVeriCode";

    }
//    @PostMapping("/verify-code")
//    public String VerifyCode(@RequestParam("verificationCode") String verificationCode) {
//        String username = verificationCodeService.UserNameAtPresent();
//        boolean code = verificationCodeService.verifyVerificationCode(username, verificationCode);
//        if (code) {
//            return "testSucess.html";
//        }
//        return "testFail.html";
//
//    }
}

