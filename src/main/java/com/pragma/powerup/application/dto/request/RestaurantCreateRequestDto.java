package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantCreateRequestDto {

  @NotBlank
  @Size(max = 100)
  @Pattern(regexp = "^(?!\\d+$).+$", message = "name cannot be only digits")
  private String name;

  @NotBlank
  @Size(max = 20)
  @Pattern(regexp = "^\\d+$", message = "nit must be numeric")
  private String nit;

  @NotBlank
  @Size(max = 200)
  private String address;

  @NotBlank
  @Size(max = 13)
  @Pattern(regexp = "^\\+?\\d{1,13}$", message = "invalid phone")
  private String phone;

  @NotBlank
  @Size(max = 255)
  private String logoUrl;

  @NotNull private Long ownerId;
}
