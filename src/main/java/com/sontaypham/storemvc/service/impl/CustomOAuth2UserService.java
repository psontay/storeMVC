package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.enums.RoleName;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.CustomOAuth2User;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.RoleRepository;
import com.sontaypham.storemvc.repository.UserRepository;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = "";
        String name = "";
        if ("google".equals(registrationId)) {
            Object emailObj = oAuth2User.getAttributes().get("email");
            Object nameObj = oAuth2User.getAttributes().get("name");
            email = emailObj != null ? emailObj.toString() : "";
            name = nameObj != null ? nameObj.toString() : "";
        }else if ("github".equals(registrationId)) {
            Object loginObj =  oAuth2User.getAttributes().get("login");
            String login = loginObj != null ? loginObj.toString() : "github_user";
            Object nameObj =  oAuth2User.getAttributes().get("name");
            if (  nameObj != null ) {
                name = nameObj.toString();
            }else {
                name = login;
            }
            Object emailObj = oAuth2User.getAttributes().get("email");
            if ( emailObj != null ) {
                email = emailObj.toString();
            }else{
                email = login + "@github.com";
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
        }else{
            Role userRole = roleRepository.findByName(RoleName.USER.name()).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
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
        user.getRoles().forEach(role -> {
           authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        String userNameAttributeName = "github".equals(registrationId) ? "id" : "email";
        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User( authorities, oAuth2User.getAttributes(), userNameAttributeName);
        return new CustomOAuth2User(defaultOAuth2User, user);
    }
}
