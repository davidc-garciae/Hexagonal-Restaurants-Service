package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.RestaurantCreateRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

  private final IRestaurantServicePort restaurantServicePort;
  private final IRestaurantRequestMapper requestMapper;
  private final IRestaurantResponseMapper responseMapper;

  @Override
  public RestaurantResponseDto create(RestaurantCreateRequestDto requestDto) {
    RestaurantModel model = requestMapper.toModel(requestDto);
    return responseMapper.toDto(restaurantServicePort.createRestaurant(model));
  }
}
