package com.bestplaces.Service.Impl;

import com.bestplaces.Dto.UserRegistrationDto;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Role;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserServiceImpl() {
    }

    @Override
    public User save(UserRegistrationDto userRegistrationDto) {
        User user = new User(userRegistrationDto.getUsername(), userRegistrationDto.getEmail(), userRegistrationDto.getPassword(), Role.NONUSER);
        user.setAvatar("http://drive.google.com/thumbnail?id=15-wqnyhbS9Pp3hRudqoQw1swnFsKyzDM");
        return userRepository.save(user);
    }

    @Transactional()
    public String getEmailByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        // Kiểm tra xem user có tồn tại không
        if (user != null) {
            return user.get().getEmail();
        } else {
            return null;
        }
    }

    @Transactional()
    public Long getUserIdByUsernames() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername(); // Gán giá trị cho biến username
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user != null) {
           return user.get().getId();
        } else {
            return null;
        }
    }
}
