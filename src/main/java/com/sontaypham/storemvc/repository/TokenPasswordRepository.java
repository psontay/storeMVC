package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenPasswordRepository extends JpaRepository<PasswordResetToken,UUID> {
    Optional<PasswordResetToken> findByTokenHash(String token);
}
