package com.bestplaces.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();
    public String generateVerificationCode(String username) {
            String code = String.format("%05d", new Random().nextInt(100000));
            VerificationCode verificationCode = new VerificationCode(code, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
            verificationCodes.put(username, verificationCode);
            return code;
    }
    public boolean verifyVerificationCode(String username, String code) {
        // Tìm username có trong list verification code
        VerificationCode verificationCode = verificationCodes.get(username);
        if (verificationCode != null && verificationCode.getCode().equals(code) && System.currentTimeMillis() < verificationCode.getExpirationTime()) {
            // Loại bỏ mã xác minh sau khi xác minh thành công
            verificationCodes.remove(username);
            // user đang null................................
//            userRepository.setAuthenticatedByUsername(UserNameAtPresent());
            userRepository.setUser(UserNameAtPresent());
            return true;
        }
        return false;
    }

    // Lớp để lưu trữ mã xác minh và thời gian hết hạn
    private static class VerificationCode {
        private final String code;
        private final long expirationTime;

        public VerificationCode(String code, long expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }
        public String getCode() {
            return code;
        }
        public long getExpirationTime() {
            return expirationTime;
        }
    }
    public String UserNameAtPresent() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null; // Khởi tạo biến username với giá trị mặc định là null
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername(); // Gán giá trị cho biến username
        }
        return username;
//        UserServiceImpl user = new UserServiceImpl();
//        user.getCurrentUserEmail();
//        return user.toString();
    }
}


