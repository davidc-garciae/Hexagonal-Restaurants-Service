package com.pragma.powerup.application.dto.request;

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
public class PlateUpdateRequestDto {

    @NotNull
    @Positive
    @Min(1)
    private Integer price;

    @NotBlank
    @Size(max = 1000)
    private String description;
}
