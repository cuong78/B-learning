package com.example.demo.service;


import com.example.demo.entity.Account;
import com.example.demo.repository.AuthenticationRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;



    @Value("${jwt.expiration.ms}")
    private int jwtExpirationMs;



    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // tao token
    public String generateToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getUsername()) // Subject is username
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey()) // Sử dụng SecretKey để verify
                .build()
                .parseSignedClaims(token) // Parse JWT
                .getPayload(); // Lấy payload (claims)
        return claims.getSubject(); // Trả về username (subject)
    }


    // verify token
    // TokenService.java

    public Account getAccountByToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String username = claims.getSubject(); // Get username from subject

        // Retrieve account by username
        Account account = authenticationRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found with username: " + username));

        return account;
    }
    public boolean validateToken(String authToken) {
        try {
            // Parse và xác thực JWT
            Jwts.parser()
                    .verifyWith(getSigninKey()) // Sử dụng SecretKey để xác thực
                    .build()
                    .parseSignedClaims(authToken); // Parse JWT
            return true; // Nếu không có lỗi, token hợp lệ
        } catch (Exception e) {
            return false; // Nếu có lỗi, token không hợp lệ
        }
    }


}
