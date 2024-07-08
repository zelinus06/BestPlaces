package com.bestplaces.Controller;

import com.bestplaces.Dto.UserRegistrationDto;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.EmailService;
import com.bestplaces.Service.Impl.EmailServiceImpl;
import com.bestplaces.Service.UsersService;
import com.bestplaces.Service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/forgot")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private UsersService usersService;

    @GetMapping()
    public String InputEmail() {
        return "EmailAccount";
    }

    @GetMapping("/recover")
    public String Recover(Model model) {
        String username = (String) model.getAttribute("username");
        model.addAttribute("username", username);
        return "RecoverPassword";
    }

    @GetMapping("/changePassword")
    public String showChangePassword(Model model) {
        String username = (String) model.getAttribute("username");
        model.addAttribute("username", username);
        return "ChangePassword";
    }

    @PostMapping("/verify-email")
    public String VerifyEmail(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("email") String to, @RequestParam(value = "cc", required = false) String[] cc, String subject, String body, Model model) {
        User existingEmail = userRepository.findByEmail(to);
        if (existingEmail != null) {
            emailService.sendMail(file, to, cc, subject, body, existingEmail.getUsername());
            model.addAttribute("username", existingEmail.getUsername());
            return "RecoverPassword";
        }
            return "redirect:/forgot"; //Viet sau
    }

    @PostMapping("/verify-code")
    public String VerifyCode(@RequestParam("verificationCode") String verificationCode, Model model, @RequestParam("username") String username) {
        boolean code = verificationCodeService.verifyVerificationCode(username, verificationCode);
        if (code) {
            model.addAttribute("username", username);
            return "ChangePassword";
        }
        model.addAttribute("errors", "Invalid verification code");
        return "RecoverPassword";
    }

    @PostMapping("/reset-password")
    public String ChangePassword(@RequestParam("username") String username,
                                 @RequestParam("newPassword") String newPassword) {
        usersService.changePassword(username, newPassword);
        return "redirect:/login";
    }
}
