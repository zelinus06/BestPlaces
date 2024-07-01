
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        String redirectUrl = "/home?page=1";
        boolean isNonUser = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_NONUSER"));
        if (isNonUser) {
            redirectUrl = "/mail";
        }
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
