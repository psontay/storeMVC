package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.enums.RoleName;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.CustomOAuth2User;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.RoleRepository;
import com.sontaypham.storemvc.repository.UserRepository;

import java.util.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  UserRepository userRepository;
  RoleRepository roleRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
    String email = "";
    String name = "";
    if ("google".equals(registrationId)) {
        email = (String) attributes.get("email");
        name = (String) attributes.get("name");
    } else if ("github".equals(registrationId)) {
        String login = (String) attributes.get("login");
        name = (String) attributes.get("name");
        if (name == null) name = login;

        email = (String) attributes.get("email");
        if (email == null) {
            email = login + "@github.com";
            attributes.put("email", email);
        }
    }
    Optional<User> isExistingUser = userRepository.findByEmail(email);
    User user;
    if (isExistingUser.isPresent()) {
      user = isExistingUser.get();
      if (!name.isEmpty()) {
        user.setFullName(name);
        userRepository.save(user);
      }
    } else {
      Role userRole =
          roleRepository
              .findByName(RoleName.USER.name())
              .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
      user =
          User.builder()
              .username(email)
              .email(email)
              .fullName(name)
              .password("")
              .roles(new HashSet<>(Collections.singletonList(userRole)))
              .build();
      userRepository.save(user);
    }
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    user.getRoles()
        .forEach(
            role -> {
              authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            });
    String userNameAttributeName =
        userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();
      if (!StringUtils.hasText(userNameAttributeName)) {
          userNameAttributeName = "github".equals(registrationId) ? "login" : "email";
      }
      if (!oAuth2User.getAttributes().containsKey(userNameAttributeName)) {
          userNameAttributeName = "id";
      }
    DefaultOAuth2User defaultOAuth2User =
        new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
    return new CustomOAuth2User(defaultOAuth2User, user);
  }
}
