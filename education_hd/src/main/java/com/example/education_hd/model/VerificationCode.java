package com.example.education_hd.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("verification_codes")
public class VerificationCode {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("email")
    private String email;
    
    @TableField("code")
    private String code;
    
    @TableField("type")
    private Integer type;
    
    @TableField("expired_at")
    private LocalDateTime expiredAt;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("is_used")
    private Boolean isUsed;
} 