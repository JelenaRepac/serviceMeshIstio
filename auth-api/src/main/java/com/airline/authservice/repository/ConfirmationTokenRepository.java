package com.airline.authservice.repository;

import com.airline.authservice.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    ConfirmationToken findByRefreshToken(String refreshToken);
}