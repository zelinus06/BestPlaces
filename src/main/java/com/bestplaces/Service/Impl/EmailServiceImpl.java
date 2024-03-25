package com.bestplaces.Service.Impl;

import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.EmailService;
import com.bestplaces.Service.MyUserDetailsService;
import com.bestplaces.Service.UserService;
import com.bestplaces.Service.VerificationCodeService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailServiceImpl implements  EmailService{

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    @Override
    public String sendMail(MultipartFile file, String to , String[] cc, String subject, String body) {
        try {
            body = verificationCodeService.generateVerificationCode(this.verificationCodeService.UserNameAtPresent());
            to = userService.getEmailByUsername(this.verificationCodeService.UserNameAtPresent());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            if (cc != null && cc.length > 0) {
                mimeMessageHelper.setCc(cc);
            }
            mimeMessageHelper.setSubject("test");
            mimeMessageHelper.setText(body);
            javaMailSender.send(mimeMessage);
            return "mail send";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
