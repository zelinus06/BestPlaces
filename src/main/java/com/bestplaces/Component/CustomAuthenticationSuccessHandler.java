
package com.bestplaces.Component;

import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.MyUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private MyUserDetailsService userDetailsService;

    public CustomAuthenticationSuccessHandler(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Lấy thông tin người dùng từ UserDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        String imageUrl = null;
        if(user.isPresent()) {
            User users = user.get();
            imageUrl = users.getAvatar();
        } else {
            System.out.println("no");
        }

        // Tạo cookie để lưu thông tin người dùng
        Cookie usernameCookie = new Cookie("username", userDetails.getUsername());
        Cookie avatarUrlCookie = new Cookie("avatarUrl", imageUrl);

        // Đặt thời gian sống của cookie (ví dụ: 30 ngày)
        int cookieMaxAge = 30 * 24 * 60 * 60; // 30 days
        usernameCookie.setMaxAge(cookieMaxAge);
        avatarUrlCookie.setMaxAge(cookieMaxAge);

        // Đặt đường dẫn của cookie
        usernameCookie.setPath("/");
        avatarUrlCookie.setPath("/");

        // Thêm cookie vào response
        response.addCookie(usernameCookie);
        response.addCookie(avatarUrlCookie);

        // Kiểm tra vai trò của người dùng
        boolean isNonUser = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_NONUSER"));
        String redirectUrl = "/home/?page=1";
        if (isNonUser) {
            redirectUrl = "/mail";
        }
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
