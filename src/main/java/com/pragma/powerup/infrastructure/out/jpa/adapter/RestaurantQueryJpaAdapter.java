package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantQueryPort;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantQueryJpaAdapter implements IRestaurantQueryPort {

    private final IRestaurantRepository repository;
    private final IRestaurantEntityMapper mapper;

    @Override
    public RestaurantModel findById(Long restaurantId) {
        return repository.findById(restaurantId).map(mapper::toModel).orElse(null);
    }
}
