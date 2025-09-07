package com.payments.settlement_system.repository;

import com.payments.settlement_system.model.BlocklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlocklistedTokenRepository extends JpaRepository<BlocklistedToken, Long> {
    Optional<BlocklistedToken> findByToken(String token);
}
