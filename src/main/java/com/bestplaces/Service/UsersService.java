package com.bestplaces.Service;

import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            User guestUser = new User();
            guestUser.setUsername("user");
            guestUser.setAvatar("http://drive.google.com/thumbnail?id=15-wqnyhbS9Pp3hRudqoQw1swnFsKyzDM");
            return guestUser;
        } else {
            // Nếu có thông tin xác thực, lấy tên người dùng và không truy cập vào cơ sở dữ liệu
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userRepository.findByUsername(username);
            return user.get();
        }
    }

    @Transactional
    public User changePassword(String username, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            entityManager.merge(user);
            return user;
        }
        else {
            return null;
        }
    }
}
