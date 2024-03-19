package com.bestplaces.Controller;

import com.bestplaces.Dto.UserRegistrationDto;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Role;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        Optional<User> existingUser = userRepository.findByUsername(registrationDto.getUsername());
        User existingEmail = userRepository.findByEmail(registrationDto.getEmail());
        if (existingUser.isPresent()) {
            result.rejectValue("username", null, "Username đã tồn tại. Vui lòng chọn một username khác.");
            model.addAttribute("error", "Username đã tồn tại. Vui lòng chọn một username khác.");
            return "registration";
        }else {
            if (existingEmail != null) {
                result.rejectValue("email", null, "Email đã tồn tại. Vui lòng chọn một email khác.");
                model.addAttribute("error", "Email đã tồn tại. Vui lòng chọn một email khác.");
                return "registration";
            }
            else{
            registrationDto.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            userService.save(registrationDto);
            return "redirect:/registration?success";
            }
        }
    }
}

