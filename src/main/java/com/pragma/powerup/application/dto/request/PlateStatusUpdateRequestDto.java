package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlateStatusUpdateRequestDto {
  @NotNull private Boolean active;
}
