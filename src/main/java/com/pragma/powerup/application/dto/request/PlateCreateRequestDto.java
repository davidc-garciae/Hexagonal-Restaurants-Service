package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.domain.model.PlateCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlateCreateRequestDto {
  @NotBlank
  @Size(max = 100)
  private String name;

  @NotNull
  @Positive
  @Min(1)
  private Integer price;

  @NotBlank
  @Size(max = 1000)
  private String description;

  @NotBlank
  @Size(max = 255)
  private String imageUrl;

  @NotNull private PlateCategory category;

  @NotNull private Long restaurantId;
}
