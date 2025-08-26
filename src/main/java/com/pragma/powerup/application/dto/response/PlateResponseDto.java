package com.pragma.powerup.application.dto.response;

import com.pragma.powerup.domain.model.PlateCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlateResponseDto {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private PlateCategory category;
    private boolean active;
    private Long restaurantId;
}
