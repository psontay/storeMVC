package com.sontaypham.storemvc.util;

import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.CustomOAuth2User;
import com.sontaypham.storemvc.model.CustomUserDetails;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtilStatic {
  public static UUID getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication.getPrincipal().equals("anonymousUser")) {
      throw new ApiException(ErrorCode.UNAUTHENTICATED);
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof CustomUserDetails) {
      return ((CustomUserDetails) principal).getId();
    } else if (principal instanceof CustomOAuth2User) {
      return ((CustomOAuth2User) principal).getUser().getId();
    }
    return null;
  }

  public static String getCurrentUsername() {
    CustomUserDetails customUserDetails = getCustomUserDetails();
    return customUserDetails.getUsername();
  }

  private static CustomUserDetails getCustomUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !(authentication.getPrincipal() instanceof CustomUserDetails customUserDetails))
      throw new ApiException(ErrorCode.UNAUTHENTICATED);
    return customUserDetails;
  }
}
