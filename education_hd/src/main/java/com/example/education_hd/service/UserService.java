package com.example.education_hd.service;

import com.example.education_hd.dto.RegisterRequest;
import com.example.education_hd.entity.User;
import com.example.education_hd.entity.VerificationCode;
import com.example.education_hd.repository.UserRepository;
import com.example.education_hd.repository.VerificationCodeRepository;
import com.example.education_hd.exception.BusinessException;
import com.example.education_hd.enums.VerificationCodeType;
import com.example.education_hd.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // 生成6位数字验证码
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    // 发送验证码
    @Transactional
    public void sendVerificationCode(String email, Integer type) {
        // 验证类型是否有效
        try {
            VerificationCodeType.fromCode(type);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("不支持的验证码类型");
        }

        // 检查邮箱格式
        if (!isValidEmail(email)) {
            throw new BusinessException("邮箱格式不正确");
        }

        // 生成验证码
        String code = generateVerificationCode();

        // 保存验证码
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setType(type);
        verificationCode.setExpiredAt(LocalDateTime.now().plusMinutes(15)); // 15分钟有效期
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setIsUsed(false);

        verificationCodeRepository.insert(verificationCode);

        // 发送邮件
        try {
            emailService.sendVerificationCode(email, code);
        } catch (Exception e) {
            throw new BusinessException("验证码发送失败：" + e.getMessage());
        }
    }

    // 验证邮箱格式
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // 注册用户
    @Transactional
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }

        // 验证验证码
        if (!verificationCodeRepository.isCodeValid(
                request.getEmail(), 
                request.getVerificationCode(), 
                VerificationCodeType.REGISTER.getCode())) {
            throw new BusinessException("验证码无效或已过期");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setEnabled(true);

        // 保存用户
        userRepository.insert(user);

        // 标记验证码已使用
        verificationCodeRepository.markCodeAsUsed(
                request.getEmail(), 
                request.getVerificationCode(), 
                VerificationCodeType.REGISTER.getCode());
    }

    /**
     * 验证用户登录
     */
    public User validateUser(String loginName, String password) {
        User user = findByLoginName(loginName);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!user.getEnabled()) {
            throw new BusinessException("账号已被禁用");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        return user;
    }

    /**
     * 根据用户名查找用户
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据邮箱查找用户
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByLoginName(String loginName) {
        // 先尝试按用户名查找
        User user = userRepository.findByUsername(loginName);
        if (user == null) {
            // 如果没找到，尝试按邮箱查找
            user = userRepository.findByEmail(loginName);
        }
        return user;
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.updatePassword(user.getUsername(), user.getPassword());
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}