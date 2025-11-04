package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.model.CustomUserDetails;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  public CustomUserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
    Set<GrantedAuthority> authorities =
        Stream.concat(
                user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())),
                Stream.concat(
                    user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(Permission::getName)
                        .map(SimpleGrantedAuthority::new),
                    user.getPermissions() == null
                        ? Stream.empty()
                        : user.getPermissions().stream()
                            .map(Permission::getName)
                            .map(SimpleGrantedAuthority::new)))
            .collect(Collectors.toSet());
    return new CustomUserDetails(user, authorities);
  }
}
