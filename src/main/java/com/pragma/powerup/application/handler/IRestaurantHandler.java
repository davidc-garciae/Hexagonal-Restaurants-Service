package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantCreateRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantListItemDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import java.util.List;

public interface IRestaurantHandler {
  RestaurantResponseDto create(RestaurantCreateRequestDto requestDto);

  List<RestaurantListItemDto> list(int page, int size);

  RestaurantResponseDto findById(Long restaurantId);
}
