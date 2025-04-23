package com.example.education_hd.repository;

import com.example.education_hd.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserRepository {
    
    @Insert("INSERT INTO users (username, password, email, enabled, created_at, updated_at) " +
            "VALUES (#{username}, #{password}, #{email}, #{enabled}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE username = #{username})")
    boolean existsByUsername(String username);

    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
    boolean existsByEmail(String email);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    @Update("UPDATE users SET password = #{password}, updated_at = NOW() " +
            "WHERE username = #{username}")
    int updatePassword(@Param("username") String username, @Param("password") String password);
}