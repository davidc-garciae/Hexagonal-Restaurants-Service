package com.pragma.powerup.infrastructure.out.feign.mapper;

import com.pragma.powerup.application.dto.response.UserInfoResponseDto;
import com.pragma.powerup.domain.model.RoleEnum;
import com.pragma.powerup.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserFeignMapper {

  @Mapping(source = "role", target = "role", qualifiedByName = "mapRole")
  UserModel toUserModel(UserInfoResponseDto userInfoResponseDto);

  @Named("mapRole")
  default RoleEnum mapRole(String role) {
    if (role == null) {
      return null;
    }
    try {
      return RoleEnum.valueOf(role.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
