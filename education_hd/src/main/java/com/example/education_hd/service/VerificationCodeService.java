package com.example.education_hd.service;

import com.example.education_hd.entity.VerificationCode;
import com.example.education_hd.repository.VerificationCodeRepository;
import com.example.education_hd.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationCodeService {
    
    private static final Logger log = LoggerFactory.getLogger(VerificationCodeService.class);
    
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Transactional
    public void sendCode(String email, Integer type) {
        try {
            // 生成验证码
            String code = generateRandomCode();
            
            // 保存验证码到数据库
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setEmail(email);
            verificationCode.setCode(code);
            verificationCode.setType(type);
            verificationCode.setExpiredAt(LocalDateTime.now().plusMinutes(10));
            verificationCode.setCreatedAt(LocalDateTime.now());
            verificationCode.setIsUsed(false);
            
            verificationCodeRepository.insert(verificationCode);
            
            // 发送邮件
            sendEmail(email, code);
        } catch (MailException e) {
            log.error("发送邮件失败", e);
            throw new BusinessException("邮件发送失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("验证码发送失败", e);
            throw new BusinessException("验证码发送失败：" + e.getMessage());
        }
    }
    
    public boolean verifyCode(String email, String code, Integer type) {
        return verificationCodeRepository.isCodeValid(email, code, type);
    }
    
    public void markAsUsed(String email, String code, Integer type) {
        verificationCodeRepository.markCodeAsUsed(email, code, type);
    }
    
    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
    
    private void sendEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("密码重置验证码");
            message.setText("您的密码重置验证码是：" + code + "，有效期为10分钟。如非本人操作，请忽略此邮件。");
            
            mailSender.send(message);
            log.info("验证码邮件发送成功，接收邮箱：{}", toEmail);
        } catch (MailException e) {
            log.error("邮件发送失败", e);
            throw new BusinessException("邮件发送失败，请检查邮箱配置");
        }
    }
} 