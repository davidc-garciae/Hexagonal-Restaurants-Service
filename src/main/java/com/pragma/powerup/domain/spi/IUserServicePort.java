package com.pragma.powerup.domain.spi;

public interface IUserServicePort {
  boolean isOwnerRole(Long userId);
}
