package com.example.demo.entity.response;

import com.example.demo.enums.RoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AuthenticationResponse {
    public long id;
    public String fullName;
    public String username;
    public String email;
    @Enumerated(value = EnumType.STRING)
    public RoleEnum roleEnum;
    public String token;
    public String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AuthenticationResponse() {

    }

    public AuthenticationResponse(long id, String fullName, String username, String email, RoleEnum roleEnum, String token) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.roleEnum = roleEnum;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
