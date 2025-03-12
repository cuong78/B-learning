package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.request.AccountRequest;
import com.example.demo.entity.request.AuthenticationRequest;
import com.example.demo.entity.response.AuthenticationResponse;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    public Account register(AccountRequest accountRequest){
        Account account = new Account();

        account.setUsername(accountRequest.getUsername());
        account.setRoleEnum(RoleEnum.CUSTOMER);
        account.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        account.setFullName(accountRequest.getFullName());
        account.setEmail(accountRequest.getEmail());

        Account newAccount = authenticationRepository.save(account);
        return newAccount;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticationRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    }


    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        }catch (Exception e){
            throw new NullPointerException("Wrong uername or password");
        }
         Account account = authenticationRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(account);
        String token = tokenService.generateToken(account);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setEmail(account.getEmail());
        authenticationResponse.setId(account.getId());
        authenticationResponse.setFullName(account.getFullName());
        authenticationResponse.setUsername(account.getUsername());
        authenticationResponse.setRoleEnum(account.getRoleEnum());
        authenticationResponse.setToken(token);
        authenticationResponse.setRefreshToken(refreshToken.getToken()); // Add this line

        return authenticationResponse;

    }


    @Autowired
    private EmailService emailService;
 /// //////////////FORGOT-PASSWORD//////////////
    public void createPasswordResetTokenForAccount(Account account, String token) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setAccount(account);
        resetToken.setExpiryDate(calculateExpiryDate(60 * 60)); // 1 giờ
        passwordResetTokenRepository.save(resetToken);
    }

    private Date calculateExpiryDate(int expiryTimeInSeconds) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, expiryTimeInSeconds);
        return new Date(cal.getTime().getTime());
    }
 /// //RESET-PASSWORD////////////////////////
 public Account validatePasswordResetToken(String token) {
     PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);

     if (passToken.getExpiryDate().before(new Date())) {
         throw new IllegalArgumentException("Token expired");
     }
     return passToken.getAccount();
 }
    public void changePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        authenticationRepository.save(account);
    }

    public void deleteResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        passwordResetTokenRepository.delete(resetToken);
    }


}
