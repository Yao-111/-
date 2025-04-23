package com.example.education_hd.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "用户名/邮箱不能为空")
    private String loginName;  // 可以是用户名或邮箱

    @NotBlank(message = "密码不能为空")
    private String password;
} 