package com.bestplaces.Controller;

import com.bestplaces.Entity.User;
import com.bestplaces.Service.EmailService;
import com.bestplaces.Service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
public class SendMailController {
    private EmailService emailService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    public SendMailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @GetMapping("/mail")
    public String showMailForm(Model model) {
        model.addAttribute("user", new User());
        String username = verificationCodeService.UserNameAtPresent();
        return "mail";
    }
    @PostMapping("/sendEmail")
    public String sendMail(@RequestParam(value = "file", required = false)MultipartFile file, String to, @RequestParam(value = "cc", required = false) String[] cc, String subject, String body) {
        emailService.sendMail(file, to, cc, subject, body);
        return "mail";
    }
    @PostMapping("/verify-code")
    public String VerifyCode(@RequestParam("verificationCode") String verificationCode) {
        String username = verificationCodeService.UserNameAtPresent();
        boolean code = verificationCodeService.verifyVerificationCode(username, verificationCode);
        if (code) {
            return "testSucess.html";
        }
        return "testFail.html";

    }
}
