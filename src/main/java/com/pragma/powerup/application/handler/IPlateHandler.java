package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.request.PlateStatusUpdateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.domain.model.PlateCategory;
import java.util.List;

public interface IPlateHandler {
  PlateResponseDto create(PlateCreateRequestDto requestDto, Long ownerId);

  PlateResponseDto update(Long plateId, PlateUpdateRequestDto requestDto, Long ownerId);

  PlateResponseDto updateStatus(Long plateId, PlateStatusUpdateRequestDto requestDto, Long ownerId);

  List<PlateResponseDto> listByRestaurant(
      Long restaurantId, PlateCategory category, int page, int size);
}
