package com.bestplaces.Controller;

import com.bestplaces.Entity.User;
import com.bestplaces.Service.MyTelegramBot;
import com.bestplaces.Service.MyUserDetailsService;
import com.bestplaces.Service.VerificationCodeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PhoneNumberController {
    @Autowired
    private MyTelegramBot myTelegramBot;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping("/addPhoneNumber")
    public String showVerifyCodeForm() {
        return "AddPhoneNumber";
    }

    @PostMapping("/generate-code")
    public String generateCode(@RequestParam(value = "phoneNumber") String phoneNumber, HttpServletResponse response) {
            Cookie cookie = new Cookie("phoneNumber", phoneNumber);
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
        String phoneNumbers = formatPhoneNumber(phoneNumber);
        String number = myTelegramBot.phoneNumberMap.get(phoneNumbers);
        if (number != null)
            myTelegramBot.sendVerificationCode(number);
        else {
            return "redirect:/addPhoneNumber";
        }
            return "AddPhoneNumber";
    }
    @PostMapping("/verify-code-phone")
    public String VerifyCode(@RequestParam("verificationCode") String verificationCode, HttpServletRequest request) {
        Cookie[] cookie = request.getCookies();
        String phoneNumber = null;
        if (cookie != null) {
            for (Cookie name : cookie) {
                if (name.getName().equals("phoneNumber")) {
                    phoneNumber = name.getValue();
                    break;
                }
            }
        }
        String username = userDetailsService.UserNameAtPresent();
        boolean code = verificationCodeService.verifyVerificationCode(username, verificationCode, phoneNumber);
        if (code) {
            return "redirect:/user";
        } else {
            return "redirect:/post";
        }
    }
    public String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("0")) {
            return "+84" + phoneNumber.substring(1);
        }
        return phoneNumber;
    }
}

