package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserServicePort;
import java.util.regex.Pattern;

public class RestaurantUseCase implements IRestaurantServicePort {

  private final IRestaurantPersistencePort restaurantPersistencePort;
  private final IUserServicePort userServicePort;

  private static final Pattern ONLY_DIGITS = Pattern.compile("^\\d+$");
  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{1,13}$");

  public RestaurantUseCase(
      IRestaurantPersistencePort restaurantPersistencePort, IUserServicePort userServicePort) {
    this.restaurantPersistencePort = restaurantPersistencePort;
    this.userServicePort = userServicePort;
  }

  @Override
  public RestaurantModel createRestaurant(RestaurantModel restaurant) {
    if (restaurant == null) {
      throw new DomainException("restaurant is required");
    }

    if (!userServicePort.isOwnerRole(restaurant.getOwnerId())) {
      throw new DomainException("OWNER role required");
    }

    if (restaurant.getName() == null
        || restaurant.getName().isBlank()
        || ONLY_DIGITS.matcher(restaurant.getName()).matches()) {
      throw new DomainException("name cannot be only digits");
    }

    if (restaurant.getPhone() == null || !PHONE_PATTERN.matcher(restaurant.getPhone()).matches()) {
      throw new DomainException("invalid phone");
    }

    if (restaurantPersistencePort.existsByNit(restaurant.getNit())) {
      throw new DomainException("NIT already exists");
    }

    return restaurantPersistencePort.save(restaurant);
  }
}
