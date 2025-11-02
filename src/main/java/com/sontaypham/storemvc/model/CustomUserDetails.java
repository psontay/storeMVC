package com.sontaypham.storemvc.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetails implements UserDetails {
  UUID id;
  String username;
  String password;
  Collection<? extends GrantedAuthority> authorities;

  public CustomUserDetails(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.password = user.getPassword();

    this.authorities = buildAuthorities(user.getRoles(), user.getPermissions());
  }

  public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.authorities = authorities;
  }

  private Collection<? extends GrantedAuthority> buildAuthorities(
      Set<Role> roles, Set<Permission> permissions) {
    // Map roles => ROLE_ADMIN, ROLE_USER,...
    Set<GrantedAuthority> roleAuthorities =
        roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toSet());

    // Map permissions => giữ nguyên tên
    Set<GrantedAuthority> permissionAuthorities =
        permissions.stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toSet());

    roleAuthorities.addAll(permissionAuthorities);
    return roleAuthorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
