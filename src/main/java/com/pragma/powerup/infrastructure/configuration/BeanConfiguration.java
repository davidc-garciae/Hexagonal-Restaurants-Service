package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IPlateQueryServicePort;
import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.api.IRestaurantQueryServicePort;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IPlateQueryPort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantQueryPort;
import com.pragma.powerup.domain.spi.IUserServicePort;
import com.pragma.powerup.domain.usecase.PlateQueryUseCase;
import com.pragma.powerup.domain.usecase.PlateUseCase;
import com.pragma.powerup.domain.usecase.RestaurantQueryUseCase;
import com.pragma.powerup.domain.usecase.RestaurantUseCase;
import com.pragma.powerup.infrastructure.out.jpa.adapter.PlateJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.PlateQueryJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.RestaurantQueryJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPlateEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPlateRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import com.pragma.powerup.infrastructure.out.users.UsersServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
  private final IRestaurantRepository restaurantRepository;
  private final IRestaurantEntityMapper restaurantEntityMapper;

  // added fields
  private final IPlateRepository plateRepository;
  private final IPlateEntityMapper plateEntityMapper;

  @Bean
  public IRestaurantPersistencePort restaurantPersistencePort() {
    return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
  }

  @Bean
  public IUserServicePort userServicePort() {
    return new UsersServiceAdapter();
  }

  @Bean
  public IRestaurantServicePort restaurantServicePort() {
    return new RestaurantUseCase(restaurantPersistencePort(), userServicePort());
  }

  // added beans
  @Bean
  public IRestaurantQueryPort restaurantQueryPort() {
    return new RestaurantQueryJpaAdapter(restaurantRepository, restaurantEntityMapper);
  }

  @Bean
  public IPlatePersistencePort platePersistencePort() {
    return new PlateJpaAdapter(plateRepository, plateEntityMapper);
  }

  @Bean
  public IPlateServicePort plateServicePort() {
    return new PlateUseCase(platePersistencePort(), restaurantQueryPort());
  }

  @Bean
  public IPlateQueryPort plateQueryPort() {
    return new PlateQueryJpaAdapter(plateRepository, plateEntityMapper);
  }

  @Bean
  public IPlateQueryServicePort plateQueryServicePort() {
    return new PlateQueryUseCase(plateQueryPort());
  }

  @Bean
  public IRestaurantQueryServicePort restaurantQueryServicePort() {
    return new RestaurantQueryUseCase(restaurantQueryPort());
  }
}
