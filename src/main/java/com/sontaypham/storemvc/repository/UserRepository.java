package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findById(UUID id);

  Optional<User> findByUsername(String username);

  void deleteByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
  Optional<User> findByUsernameOrEmail(String username, String email);
}
