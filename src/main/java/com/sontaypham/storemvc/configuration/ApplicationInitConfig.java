package com.sontaypham.storemvc.configuration;

import com.sontaypham.storemvc.enums.RoleName;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.PermissionRepository;
import com.sontaypham.storemvc.repository.RoleRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            PermissionRepository permissionRepository
                                       ) {
        return args -> {
            log.warn("Init admin application is running");
            try {
                // create || add permissions
                List<String> allPermissions = List.of("USER_CREATE" , "USER_READ", "USER_UPDATE", "USER_DELETE" ,
                                                      "CATEGORY_CREATE", "CATEGORY_READ", "CATEGORY_UPDATE",
                                                      "CATEGORY_DELETE" , "PERMISSION_CREATE" , "PERMISSION_READ" ,
                                                      "PERMISSION_UPDATE" , "PERMISSION_DELETE" , "PRODUCT_CREATE" ,
                                                      "PRODUCT_READ", "PRODUCT_UPDATE", "PRODUCT_DELETE" ,
                                                      "ROLE_CREATE" , "ROLE_READ", "ROLE_UPDATE", "ROLE_DELETE" ,
                                                      "SUPPLIER_CREATE" , "SUPPLIER_READ" , "SUPPLIER_UPDATE" ,
                                                      "SUPPLIER_DELETE");
                for ( String permission : allPermissions) {
                    if ( permissionRepository.findByName(permission).isEmpty() ) {
                        permissionRepository.save( new Permission(permission , "Permission for : " +  permission));
                    }
                }
                Set<Permission> permissions = new HashSet<>(permissionRepository.findAll());
                // create || add roles
                Role adminRole = roleRepository.findRoleByName(RoleName.ADMIN.name()).orElseGet( () -> {
                    Role role =
                            Role.builder().name(RoleName.ADMIN.name()).description("ADMIN ROLE").permissions(permissions).build();
                    return roleRepository.save(role);
                });
                roleRepository.save(adminRole);
                if ( userRepository.findByUsername(adminUsername).isEmpty() ) {
                    User admin = User.builder()
                            .username(adminUsername)
                            .password(passwordEncoder.encode(adminPassword))
                            .email(adminEmail)
                            .address("null")
                            .telPhone("null")
                            .fullName("Sơn Tây Phạm")
                            .roles(Set.of(adminRole))
                            .permissions(permissions)
                                     .build();
                    userRepository.save(admin);
                    log.warn("Admin has been created with default username : " + adminUsername);
                }
            }catch (Exception e) {
                log.error("Error in init admin application : " , e);
            }
        };
    }
}
