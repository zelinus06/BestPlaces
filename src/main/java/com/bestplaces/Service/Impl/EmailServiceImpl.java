package com.bestplaces.Service.Impl;

import com.bestplaces.Service.EmailService;
import com.bestplaces.Service.MyUserDetailsService;
import com.bestplaces.Service.VerificationCodeService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Override
    public String sendMail(MultipartFile file, String to , String[] cc, String subject, String body) {
        try {
            body = "Mã xác nhận của bạn là: " + verificationCodeService.generateVerificationCode(myUserDetailsService.UserNameAtPresent());
            to = userService.getEmailByUsername(myUserDetailsService.UserNameAtPresent());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            if (cc != null && cc.length > 0) {
                mimeMessageHelper.setCc(cc);
            }
            mimeMessageHelper.setSubject("Mã xác nhận đăng ký tài khoản BestPlaces");
            mimeMessageHelper.setText(body);
            javaMailSender.send(mimeMessage);
            return "mail send";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String sendMail(MultipartFile file, String to, String[] cc, String subject, String body, String username) {
        try {
            body = "Mã xác nhận của bạn là: " + verificationCodeService.generateVerificationCode(username);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            if (cc != null && cc.length > 0) {
                mimeMessageHelper.setCc(cc);
            }
            mimeMessageHelper.setSubject("Mã xác nhận tài khoản BestPlaces");
            mimeMessageHelper.setText(body);
            javaMailSender.send(mimeMessage);
            return "mail send";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
