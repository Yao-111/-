package com.example.education_hd.repository;

import com.example.education_hd.entity.VerificationCode;
import org.apache.ibatis.annotations.*;

@Mapper
public interface VerificationCodeRepository {
    
    @Insert("INSERT INTO verification_codes (email, code, type, expired_at, created_at, is_used) " +
            "VALUES (#{email}, #{code}, #{type}, #{expiredAt}, #{createdAt}, #{isUsed})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VerificationCode code);

    @Select("SELECT EXISTS(SELECT 1 FROM verification_codes " +
            "WHERE email = #{email} AND code = #{code} AND type = #{type} " +
            "AND expired_at > NOW() AND is_used = false)")
    boolean isCodeValid(@Param("email") String email, 
                       @Param("code") String code, 
                       @Param("type") Integer type);

    @Update("UPDATE verification_codes SET is_used = true " +
            "WHERE email = #{email} AND code = #{code} AND type = #{type} " +
            "AND expired_at > NOW() AND is_used = false")
    int markCodeAsUsed(@Param("email") String email, 
                      @Param("code") String code, 
                      @Param("type") Integer type);
} 