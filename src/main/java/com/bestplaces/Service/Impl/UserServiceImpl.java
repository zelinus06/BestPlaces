package com.bestplaces.Service.Impl;

import com.bestplaces.Dto.UserRegistrationDto;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Role;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
        return userRepository.save(user);
    }

    @Transactional()
    public String getEmailByUsername(String username) {
        // Thực hiện truy vấn cơ sở dữ liệu để lấy thông tin người dùng dựa trên username
        Optional<User> user = userRepository.findByUsername(username);
        // Kiểm tra xem user có tồn tại không
        if (user != null) {
            return user.get().getEmail();
        } else {
            return null;
        }
    }
}
