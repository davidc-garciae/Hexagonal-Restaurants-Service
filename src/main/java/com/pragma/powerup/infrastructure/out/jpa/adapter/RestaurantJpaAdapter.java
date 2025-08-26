package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

  private final IRestaurantRepository repository;
  private final IRestaurantEntityMapper mapper;

  @Override
  public boolean existsByNit(String nit) {
    return repository.existsByNit(nit);
  }

  @Override
  public RestaurantModel save(RestaurantModel restaurant) {
    RestaurantEntity entity = repository.save(mapper.toEntity(restaurant));
    return mapper.toModel(entity);
  }
}
