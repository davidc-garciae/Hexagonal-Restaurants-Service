package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestaurantCreateRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantListItemDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/** Restaurant REST Controller - HU-002: Crear Restaurante (ADMIN) */
@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    /** POST /api/v1/restaurants - HU-002: Crear Restaurante (ADMIN) */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantResponseDto> create(
            @Valid @RequestBody RestaurantCreateRequestDto request, UriComponentsBuilder uriBuilder) {

        RestaurantResponseDto response = restaurantHandler.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(
                        uriBuilder.path("/api/v1/restaurants/{id}").buildAndExpand(response.getId()).toUri())
                .body(response);
    }

    /** GET /api/v1/restaurants - Listar restaurantes */
    @GetMapping
    public ResponseEntity<List<RestaurantListItemDto>> list(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<RestaurantListItemDto> response = restaurantHandler.list(page, size);
        return ResponseEntity.ok(response);
    }

    /** GET /api/v1/restaurants/{id} - Obtener restaurante por ID */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> findById(@PathVariable Long id) {
        RestaurantResponseDto response = restaurantHandler.findById(id);
        return ResponseEntity.ok(response);
    }
}
