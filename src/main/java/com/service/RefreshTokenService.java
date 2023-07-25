package com.service;

import com.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);

    public RefreshToken createRefreshToken(int employeeId);

    public RefreshToken verifyExpiration(RefreshToken token) ;
}
