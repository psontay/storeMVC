package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.user.UserRegisterRequest;
import com.sontaypham.storemvc.dto.response.user.UserRegisterResponse;
import com.sontaypham.storemvc.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRegisterMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "roles", ignore = true)
  @Mapping(target = "permissions", ignore = true)
  User toUser(UserRegisterRequest request);

  UserRegisterResponse toResponse(User user);
}
