package com.example.demo.api;

import com.example.demo.entity.Account;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.request.*;
import com.example.demo.entity.response.AuthenticationResponse;
import com.example.demo.entity.response.TokenRefreshResponse;
import com.example.demo.exception.exceptions.TokenRefreshException;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.EmailService;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api")
public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TokenService tokenService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody AccountRequest account) {
        Account newAccount = authenticationService.register(account);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getAccount)
                .map(account -> {
                    String token = tokenService.generateToken(account);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }


    @PostMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            // Tìm tài khoản bằng email
            Account account = authenticationRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));

            // Tạo token
            String token = UUID.randomUUID().toString();
            authenticationService.createPasswordResetTokenForAccount(account, token);

            // Gửi token qua email
            String emailSubject = "Reset Password Token";
            String emailText = "Your reset password token is: " + token;
            emailService.sendEmail(request.getEmail(), emailSubject, emailText);

            // Trả về thông báo thành công
            return ResponseEntity.ok("Reset token has been sent to your email.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @RequestBody ResetPasswordRequest request
    ) {
        try {
            Account account = authenticationService.validatePasswordResetToken(token);
            authenticationService.changePassword(account, request.getNewPassword());
            authenticationService.deleteResetToken(token);
            return ResponseEntity.ok("Đặt lại mật khẩu thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            // Lấy token từ header
            String token = authHeader.substring(7); // Bỏ qua "Bearer "

            // Xác thực token và lấy thông tin người dùng
            Account account = tokenService.getAccountByToken(token);

            // Xoá refresh token của người dùng
            refreshTokenRepository.deleteByAccount(account);

            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }



}
