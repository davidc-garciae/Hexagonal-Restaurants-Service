package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantCreateRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;

public interface IRestaurantHandler {
  RestaurantResponseDto create(RestaurantCreateRequestDto requestDto);
}
