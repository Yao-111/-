package com.example.education_hd.enums;

/**
 * 用户状态枚举
 */
public enum UserStatus {
    
    ENABLED(1, "正常"),
    DISABLED(0, "禁用");

    private final int code;
    private final String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static UserStatus fromCode(int code) {
        for (UserStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的用户状态: " + code);
    }
} 