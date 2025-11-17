package com.sontaypham.storemvc.configuration;

import com.sontaypham.storemvc.enums.RoleName;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.PermissionRepository;
import com.sontaypham.storemvc.repository.RoleRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class ApplicationInitConfig {
  @Value("${initAdmin.default.username}")
  String adminUsername;

  @Value("${initAdmin.default.password}")
  String adminPassword;

  @Value("${initAdmin.default.email}")
  String adminEmail;

  private final PasswordEncoder passwordEncoder;

  public ApplicationInitConfig(final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  ApplicationRunner applicationRunner(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PermissionRepository permissionRepository) {
    return args -> {
      log.warn("Init admin application is running");
      try {
        // create || add permissions
        List<String> allPermissionsName =
            List.of(
                "USER_CREATE", "USER_READ", "USER_UPDATE", "USER_DELETE", "CATEGORY_CREATE", "CATEGORY_READ", "CATEGORY_UPDATE", "CATEGORY_DELETE", "PERMISSION_CREATE", "PERMISSION_READ", "PERMISSION_UPDATE", "PERMISSION_DELETE", "PRODUCT_CREATE", "PRODUCT_READ", "PRODUCT_UPDATE", "PRODUCT_DELETE", "ROLE_CREATE", "ROLE_READ", "ROLE_UPDATE", "ROLE_DELETE", "SUPPLIER_CREATE", "SUPPLIER_READ", "SUPPLIER_UPDATE", "SUPPLIER_DELETE", "CART_ADD", "CART_READ", "CART_UPDATE", "CART_DELETE", "ORDER_CREATE", "ORDER_READ","ORDER_UPDATE", "ORDER_DELETE");
          Set<Permission> allPermissions = allPermissionsName.stream()
                                                              .map(name -> permissionRepository.findByName(name)
                                                                                               .orElseGet(() -> permissionRepository.save(new Permission(name, "Permission for : " + name))))
                                                              .collect(Collectors.toSet());
        // get all permissions of database
        Set<String> userPermissionsName =
            Set.of("CATEGORY_READ", "PRODUCT_READ", "SUPPLIER_READ", "CART_READ", "CART_ADD", "CART_UPDATE", "CART_DELETE", "ORDER_CREATE", "ORDER_READ", "ORDER_UPDATE", "ORDER_DELETE");
          Set<Permission> userPermission = userPermissionsName.stream()
                                                              .map(p -> permissionRepository.findByName(p).get())
                                                              .collect(Collectors.toSet());
          // create role
          Role adminRole = Role.builder()
                               .name(RoleName.ADMIN.name())
                               .description("Admin vo dich")
                               .permissions(allPermissions)
                               .build();
          roleRepository.save(adminRole);

          Role userRole = Role.builder()
                              .name(RoleName.USER.name())
                              .permissions(userPermission)
                              .build();
          roleRepository.save(userRole);
        userRole.setPermissions(new HashSet<>(userPermission));
        roleRepository.save(userRole);
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
          User admin =
              User.builder()
                  .username(adminUsername)
                  .password(passwordEncoder.encode(adminPassword))
                  .email(adminEmail)
                  .address("null")
                  .telPhone("null")
                  .fullName("Sơn Tây Phạm")
                  .roles(Set.of(adminRole))
                  .permissions(allPermissions)
                  .build();
          userRepository.save(admin);
          log.warn("Admin has been created with default username : " + admin.getUsername());
        }
      } catch (Exception e) {
        log.error("Error in init admin application : ", e);
      }
    };
  }
}
