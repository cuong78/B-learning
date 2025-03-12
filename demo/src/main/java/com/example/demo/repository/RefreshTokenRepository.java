package com.example.demo.repository;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByAccount(Account account);

    @Modifying
    int deleteByAccount(Account account);
}
