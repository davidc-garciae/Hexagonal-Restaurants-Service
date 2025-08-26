package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantQueryPort;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class RestaurantQueryJpaAdapter implements IRestaurantQueryPort {

  private final IRestaurantRepository repository;
  private final IRestaurantEntityMapper mapper;

  @Override
  public RestaurantModel findById(Long restaurantId) {
    return repository.findById(restaurantId).map(mapper::toModel).orElse(null);
  }

  @Override
  public List<RestaurantModel> findAllOrderedByName(int page, int size) {
    var pageable = PageRequest.of(page, size, Sort.by("name").ascending());
    return repository.findAll(pageable).map(mapper::toModel).getContent();
  }
}
