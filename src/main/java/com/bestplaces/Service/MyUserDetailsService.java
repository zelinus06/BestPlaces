package com.bestplaces.Service;

import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user = userRepository.findByUsername(username);
       if(user.isPresent()) {
           var userObj = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRole(userObj))
                    .build();
       }
       else {
           throw new UsernameNotFoundException(username);
       }
    }
    private String getRole(User user) {
        if (user.getRole() == null) {
            return "USER";
        }
        return user.getRole().toString();
    }
    }
