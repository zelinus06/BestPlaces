package com.bestplaces.Service;

import com.bestplaces.Dto.UserRegistrationDto;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Role;
import com.bestplaces.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRegistrationDto userRegistrationDto) {
        User user = new User(userRegistrationDto.getUsername(), userRegistrationDto.getEmail(), userRegistrationDto.getPassword(), Role.USER );
        return userRepository.save(user);
    }
}
