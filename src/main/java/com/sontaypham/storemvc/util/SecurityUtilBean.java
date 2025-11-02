package com.sontaypham.storemvc.util;

import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("securityUtil")
public class SecurityUtilBean {
    public UUID getUserId() {
        CustomUserDetails customUserDetails = getCustomUserDetails();
        return customUserDetails.getId();
    }
    public String getCurrentUsername() {
        CustomUserDetails customUserDetails = getCustomUserDetails();
        return customUserDetails.getUsername();
    }
    private static CustomUserDetails getCustomUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) )
            throw new ApiException(ErrorCode.UNAUTHENTICATED);
        return customUserDetails;
    }
}
