package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestaurantCreateRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.infrastructure.security.RoleConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantRestController {

  private final IRestaurantHandler restaurantHandler;

  @PostMapping
  @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
  public ResponseEntity<RestaurantResponseDto> create(
      @Valid @RequestBody RestaurantCreateRequestDto request, UriComponentsBuilder uriBuilder) {
    RestaurantResponseDto response = restaurantHandler.create(request);
    return ResponseEntity.created(
            uriBuilder.path("/api/v1/restaurants/{id}").buildAndExpand(response.getId()).toUri())
        .body(response);
  }
}
