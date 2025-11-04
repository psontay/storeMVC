package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Permission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
  Optional<Permission> findByName(String name);
}
