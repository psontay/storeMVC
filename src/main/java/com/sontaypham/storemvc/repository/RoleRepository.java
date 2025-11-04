package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findByName(String name);
}
