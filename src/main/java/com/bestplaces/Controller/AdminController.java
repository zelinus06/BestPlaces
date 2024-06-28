package com.bestplaces.Controller;

import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Role;
import com.bestplaces.Service.AdminService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping
    public String showAdminPage(Model model){
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/delete-user")
    public String deleteUser(@RequestParam("id") Long id) {
        adminService.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/edit-user")
    public String showEditUserForm(@RequestParam("id") Long id, Model model) {
        User user = adminService.getUserById(id);
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/edit-user")
    public String editUser(@RequestParam("id") Long id,
                           @RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("phoneNumber") String phoneNumber,
                           @RequestParam("selectedRole") Role selectedRole) {
        System.out.println("id: " + id + " username: " + username + " email: " + email + " phoneNumber: " + phoneNumber + " selectedRole: " + selectedRole);
        adminService.editUser(id, username, email, phoneNumber, selectedRole);
        return "redirect:/admin";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("selectedRole") Role selectedRole) {
        user.setRole(selectedRole);
        adminService.saveUser(user);
        return "redirect:/admin";
    }
}
