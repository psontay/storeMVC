package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.PasswordResetToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenPasswordRepository extends JpaRepository<PasswordResetToken, UUID> {
  Optional<PasswordResetToken> findByTokenHash(String token);
}
