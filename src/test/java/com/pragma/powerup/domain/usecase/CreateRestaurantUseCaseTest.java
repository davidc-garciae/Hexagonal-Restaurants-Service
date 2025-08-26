package com.pragma.powerup.domain.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserServicePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateRestaurantUseCaseTest {

  private final IRestaurantPersistencePort restaurantPersistencePort =
      Mockito.mock(IRestaurantPersistencePort.class);
  private final IUserServicePort userServicePort = Mockito.mock(IUserServicePort.class);

  private final IRestaurantServicePort useCase =
      new RestaurantUseCase(restaurantPersistencePort, userServicePort);

  @Test
  @DisplayName("Should create restaurant when data is valid and owner has OWNER role")
  void shouldCreateRestaurantWhenDataValid() {
    RestaurantModel toCreate =
        new RestaurantModel(
            null,
            "Pizza Planet",
            "1234567890",
            "Main St 123",
            "+573005698325",
            "http://logo.png",
            10L);

    Mockito.when(userServicePort.isOwnerRole(10L)).thenReturn(true);
    Mockito.when(restaurantPersistencePort.existsByNit("1234567890")).thenReturn(false);
    Mockito.when(restaurantPersistencePort.save(Mockito.any()))
        .thenAnswer(
            inv -> {
              RestaurantModel m = inv.getArgument(0);
              m.setId(1L);
              return m;
            });

    RestaurantModel created = useCase.createRestaurant(toCreate);

    assertThat(created.getId()).isEqualTo(1L);
    assertThat(created.getName()).isEqualTo("Pizza Planet");
  }

  @Test
  @DisplayName("Should fail when owner is not OWNER role")
  void shouldFailWhenOwnerNotOwnerRole() {
    RestaurantModel toCreate =
        new RestaurantModel(null, "Place", "111", "Addr", "123", "http://x", 20L);
    Mockito.when(userServicePort.isOwnerRole(20L)).thenReturn(false);

    assertThatThrownBy(() -> useCase.createRestaurant(toCreate))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("OWNER role required");
  }

  @Test
  @DisplayName("Should fail when nit already exists")
  void shouldFailWhenNitExists() {
    RestaurantModel toCreate =
        new RestaurantModel(null, "Place", "9999", "Addr", "123", "http://x", 20L);
    Mockito.when(userServicePort.isOwnerRole(20L)).thenReturn(true);
    Mockito.when(restaurantPersistencePort.existsByNit("9999")).thenReturn(true);

    assertThatThrownBy(() -> useCase.createRestaurant(toCreate))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("NIT already exists");
  }

  @Test
  @DisplayName("Should fail when name is only digits")
  void shouldFailWhenNameOnlyDigits() {
    RestaurantModel toCreate =
        new RestaurantModel(null, "12345", "111", "Addr", "123", "http://x", 20L);
    Mockito.when(userServicePort.isOwnerRole(20L)).thenReturn(true);

    assertThatThrownBy(() -> useCase.createRestaurant(toCreate))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("name cannot be only digits");
  }

  @Test
  @DisplayName("Should fail when phone is invalid pattern")
  void shouldFailWhenPhoneInvalid() {
    RestaurantModel toCreate =
        new RestaurantModel(null, "Place", "111", "Addr", "+57-300", "http://x", 20L);
    Mockito.when(userServicePort.isOwnerRole(20L)).thenReturn(true);

    assertThatThrownBy(() -> useCase.createRestaurant(toCreate))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("invalid phone");
  }
}
