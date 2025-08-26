package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import com.pragma.powerup.infrastructure.security.RoleConstants;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/v1/plates")
@RequiredArgsConstructor
public class PlateRestController {

    private final IPlateHandler plateHandler;

    @PostMapping
    @PreAuthorize("hasRole('" + RoleConstants.OWNER + "')")
    public ResponseEntity<PlateResponseDto> create(
            @Valid @RequestBody PlateCreateRequestDto request,
            HttpServletRequest httpRequest,
            UriComponentsBuilder uriBuilder) {

        Long ownerId = Long.valueOf(httpRequest.getHeader("X-User-Id"));
        PlateResponseDto response = plateHandler.create(request, ownerId);
        return ResponseEntity.created(
                uriBuilder.path("/api/v1/plates/{id}").buildAndExpand(response.getId()).toUri())
                .body(response);
    }
}
