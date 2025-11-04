package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.auth.AuthenticationRequest;
import com.sontaypham.storemvc.dto.response.auth.AuthenticationResponse;

public interface AuthenticationService {
  AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
