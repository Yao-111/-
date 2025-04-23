package com.example.education_hd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import javax.annotation.PostConstruct;

@Configuration
public class MailConfig {
    // Spring Boot 会自动配置 JavaMailSender
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @PostConstruct
    public void testMailConnection() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(fromEmail);  // 发送测试邮件给自己
            message.setSubject("测试邮件");
            message.setText("这是一封测试邮件，用于验证邮件服务器配置是否正确。");
            mailSender.send(message);
            System.out.println("邮件服务器配置正确！");
        } catch (Exception e) {
            System.err.println("邮件服务器配置错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
} 