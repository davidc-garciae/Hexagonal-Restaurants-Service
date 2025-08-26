package com.pragma.powerup.infrastructure.out.users;

import com.pragma.powerup.domain.spi.IUserServicePort;

// TODO: Replace with Feign client integration to usuarios-service
public class UsersServiceAdapter implements IUserServicePort {
  @Override
  public boolean isOwnerRole(Long userId) {
    // Minimal stub for now; to be replaced with real integration
    return true;
  }
}
