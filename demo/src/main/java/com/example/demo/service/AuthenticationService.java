package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.request.AccountRequest;
import com.example.demo.entity.request.AuthenticationRequest;
import com.example.demo.entity.response.AuthenticationResponse;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Account register(AccountRequest accountRequest){
        // xử lý logic

        // lưu xuống database

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
         String token = tokenService.generateToken(account);

         AuthenticationResponse authenticationResponse = new AuthenticationResponse();
         authenticationResponse.setEmail(account.getEmail());
         authenticationResponse.setId(account.getId());
         authenticationResponse.setFullName(account.getFullName());
         authenticationResponse.setUsername(account.getUsername());
         authenticationResponse.setRoleEnum(account.getRoleEnum());
         authenticationResponse.setToken(token);

         return authenticationResponse;
    }
}
