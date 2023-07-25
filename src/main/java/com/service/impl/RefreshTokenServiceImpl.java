package com.service.impl;


import com.entity.RefreshToken;
import com.exception.TokenRefreshException;
import com.repository.EmployeeRepository;
import com.repository.RefreshTokenRepository;
import com.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${com.jwt.refreshExpiration}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(int employeeId) {
        RefreshToken refreshToken = new RefreshToken();
        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime newDateTime = localDateTime.plus(Duration.ofMillis(refreshTokenDurationMs));
        refreshToken.setEmployee(employeeRepository.findById(employeeId).get());
        refreshToken.setExpiryDate(newDateTime);
        System.out.println(refreshToken);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        System.out.println(token.getExpiryDate());
        System.out.println(LocalDateTime.now());
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
