package com.example.education_hd.controller;

import com.example.education_hd.dto.*;
import com.example.education_hd.entity.User;
import com.example.education_hd.model.VerificationCode;
import com.example.education_hd.service.EmailService;
import com.example.education_hd.service.UserService;
import com.example.education_hd.service.VerificationCodeService;
import com.example.education_hd.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.education_hd.exception.BusinessException;
import org.springframework.validation.annotation.Validated;
import com.example.education_hd.enums.VerificationCodeType;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${cors.allowed-origins}")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<Void>> sendCode(@Validated @RequestBody SendCodeRequest request) {
        try {
            // 根据验证码类型进行不同的验证
            boolean emailExists = userService.existsByEmail(request.getEmail());
            
            // 获取验证码类型
            VerificationCodeType codeType = VerificationCodeType.fromCode(request.getType());
            
            switch (codeType) {
                case REGISTER:
                    // 注册验证码：邮箱不能已注册
                    if (emailExists) {
                        return ResponseEntity.badRequest()
                            .body(ApiResponse.error(400, "该邮箱已被注册"));
                    }
                    break;
                
                case RESET_PASSWORD:
                    // 重置密码验证码：邮箱必须已注册
                    if (!emailExists) {
                        return ResponseEntity.badRequest()
                            .body(ApiResponse.error(400, "该邮箱未注册"));
                    }
                    break;
                
                default:
                    return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "不支持的验证码类型"));
            }
            
            // 生成并发送验证码
            verificationCodeService.sendCode(request.getEmail(), request.getType());
            
            return ResponseEntity.ok(ApiResponse.success("验证码已发送"));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            log.error("发送验证码失败", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "服务器内部错误"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Validated @RequestBody ResetPasswordRequest request) {
        try {
            // 验证验证码
            if (!verificationCodeService.verifyCode(request.getEmail(), request.getVerificationCode(), 3)) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "验证码无效或已过期"));
            }
            
            // 重置密码
            userService.resetPassword(request.getEmail(), request.getNewPassword());
            
            // 标记验证码已使用
            verificationCodeService.markAsUsed(request.getEmail(), request.getVerificationCode(), 3);
            
            return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            log.error("重置密码失败", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "服务器内部错误"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("注册失败", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "注册失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request) {
        try {
            User user = userService.validateUser(request.getLoginName(), request.getPassword());
            
            // 生成JWT token
            String token = jwtUtil.generateToken(user.getUsername());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "登录成功");
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("登录失败", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "登录失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}