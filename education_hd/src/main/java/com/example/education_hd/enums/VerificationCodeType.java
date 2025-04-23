package com.example.education_hd.enums;

/**
 * 验证码类型枚举
 */
public enum VerificationCodeType {
    
    REGISTER(1, "注册验证码"),
    RESET_PASSWORD(3, "重置密码验证码");

    private final int code;
    private final String description;

    VerificationCodeType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VerificationCodeType fromCode(int code) {
        for (VerificationCodeType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的验证码类型: " + code);
    }
} 