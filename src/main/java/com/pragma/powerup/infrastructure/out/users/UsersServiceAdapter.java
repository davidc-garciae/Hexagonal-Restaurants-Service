package com.pragma.powerup.infrastructure.out.users;

import com.pragma.powerup.domain.model.RoleEnum;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import com.pragma.powerup.domain.spi.IUserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersServiceAdapter implements IUserServicePort {

  private final IUserFeignPort userFeignPort;

  @Override
  public boolean isOwnerRole(Long userId) {
    try {
      UserModel user = userFeignPort.getUserById(userId);
      return user != null && RoleEnum.OWNER.equals(user.getRole());
    } catch (Exception e) {
      return false;
    }
  }
}
