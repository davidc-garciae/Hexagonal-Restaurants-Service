package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.request.PlateStatusUpdateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import com.pragma.powerup.application.util.JwtSecurityUtils;
import com.pragma.powerup.domain.model.PlateCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/plates")
@RequiredArgsConstructor
public class PlateRestController {

    private final IPlateHandler plateHandler;
    private final JwtSecurityUtils jwtSecurityUtils;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<PlateResponseDto> create(
            @Valid @RequestBody PlateCreateRequestDto request, UriComponentsBuilder uriBuilder) {

        Long ownerId = jwtSecurityUtils.getCurrentUserId();
        PlateResponseDto response = plateHandler.create(request, ownerId);
        return ResponseEntity.created(
                uriBuilder.path("/api/v1/plates/{id}").buildAndExpand(response.getId()).toUri())
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<PlateResponseDto> update(
            @PathVariable("id") Long id, @Valid @RequestBody PlateUpdateRequestDto request) {

        Long ownerId = jwtSecurityUtils.getCurrentUserId();
        PlateResponseDto response = plateHandler.update(id, request, ownerId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<PlateResponseDto> updateStatus(
            @PathVariable("id") Long id, @Valid @RequestBody PlateStatusUpdateRequestDto request) {

        Long ownerId = jwtSecurityUtils.getCurrentUserId();
        PlateResponseDto response = plateHandler.updateStatus(id, request, ownerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<java.util.List<PlateResponseDto>> listByRestaurant(
            @PathVariable("id") Long restaurantId,
            @RequestParam(name = "category", required = false) PlateCategory category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        var response = plateHandler.listByRestaurant(restaurantId, category, page, size);
        return ResponseEntity.ok(response);
    }
}
