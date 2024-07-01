package com.bestplaces.Service;

import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            user.get().setUsername("Guest");
            user.get().setAvatar("http://drive.google.com/thumbnail?id=15-wqnyhbS9Pp3hRudqoQw1swnFsKyzDM");
            return user.get();
        }
    }
}
