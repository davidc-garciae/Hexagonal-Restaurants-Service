package com.pragma.powerup;

import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.request.PlateStatusUpdateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.request.RestaurantCreateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantListItemDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.model.RestaurantModel;

import java.util.List;

/**
 * Factory centralizada para crear datos de prueba consistentes
 * en todas las capas del servicio de restaurantes.
 */
public class TestDataFactory {

    // ==================== DOMAIN MODELS ====================

    /**
     * Crea un RestaurantModel válido para pruebas.
     */
    public static RestaurantModel createValidRestaurantModel() {
        return new RestaurantModel(
                1L,
                "Pizza Palace",
                "123456789",
                "123 Main St",
                "+1234567890",
                "https://logo.example.com/pizza.png",
                100L);
    }

    /**
     * Crea un RestaurantModel con datos personalizados.
     */
    public static RestaurantModel createRestaurantModel(
            Long id,
            String name,
            String nit,
            String address,
            String phone,
            String logoUrl,
            Long ownerId) {
        return new RestaurantModel(id, name, nit, address, phone, logoUrl, ownerId);
    }

    /**
     * Crea un PlateModel válido para pruebas.
     */
    public static PlateModel createValidPlateModel() {
        return new PlateModel(
                1L,
                "Margherita Pizza",
                15000,
                "Classic pizza with tomato sauce and mozzarella cheese",
                "https://images.example.com/margherita.jpg",
                PlateCategory.PRINCIPAL,
                true,
                1L);
    }

    /**
     * Crea un PlateModel con datos personalizados.
     */
    public static PlateModel createPlateModel(
            Long id,
            String name,
            Integer price,
            String description,
            String imageUrl,
            PlateCategory category,
            Boolean active,
            Long restaurantId) {
        return new PlateModel(id, name, price, description, imageUrl, category, active, restaurantId);
    }

    /**
     * Crea múltiples PlateModels para pruebas de listado.
     */
    public static List<PlateModel> createPlateModelList() {
        return List.of(
                createPlateModel(1L, "Caesar Salad", 8000, "Fresh lettuce with Caesar dressing",
                        "https://images.example.com/caesar.jpg", PlateCategory.ENTRADA, true, 1L),
                createPlateModel(2L, "Margherita Pizza", 15000, "Classic pizza with tomato sauce",
                        "https://images.example.com/margherita.jpg", PlateCategory.PRINCIPAL, true, 1L),
                createPlateModel(3L, "Tiramisu", 6000, "Traditional Italian dessert",
                        "https://images.example.com/tiramisu.jpg", PlateCategory.POSTRE, true, 1L));
    }

    // ==================== REQUEST DTOs ====================

    /**
     * Crea un RestaurantCreateRequestDto válido.
     */
    public static RestaurantCreateRequestDto createValidRestaurantCreateRequest() {
        RestaurantCreateRequestDto dto = new RestaurantCreateRequestDto();
        dto.setName("Pizza Palace");
        dto.setNit("123456789");
        dto.setAddress("123 Main St");
        dto.setPhone("+1234567890");
        dto.setLogoUrl("https://logo.example.com/pizza.png");
        dto.setOwnerId(100L);
        return dto;
    }

    /**
     * Crea un RestaurantCreateRequestDto con datos personalizados.
     */
    public static RestaurantCreateRequestDto createRestaurantCreateRequest(
            String name,
            String nit,
            String address,
            String phone,
            String logoUrl,
            Long ownerId) {
        RestaurantCreateRequestDto dto = new RestaurantCreateRequestDto();
        dto.setName(name);
        dto.setNit(nit);
        dto.setAddress(address);
        dto.setPhone(phone);
        dto.setLogoUrl(logoUrl);
        dto.setOwnerId(ownerId);
        return dto;
    }

    /**
     * Crea un PlateCreateRequestDto válido.
     */
    public static PlateCreateRequestDto createValidPlateCreateRequest() {
        PlateCreateRequestDto dto = new PlateCreateRequestDto();
        dto.setName("Margherita Pizza");
        dto.setPrice(15000);
        dto.setDescription("Classic pizza with tomato sauce and mozzarella cheese");
        dto.setImageUrl("https://images.example.com/margherita.jpg");
        dto.setCategory(PlateCategory.PRINCIPAL);
        dto.setRestaurantId(1L);
        return dto;
    }

    /**
     * Crea un PlateCreateRequestDto con datos personalizados.
     */
    public static PlateCreateRequestDto createPlateCreateRequest(
            String name,
            Integer price,
            String description,
            String imageUrl,
            PlateCategory category,
            Long restaurantId) {
        PlateCreateRequestDto dto = new PlateCreateRequestDto();
        dto.setName(name);
        dto.setPrice(price);
        dto.setDescription(description);
        dto.setImageUrl(imageUrl);
        dto.setCategory(category);
        dto.setRestaurantId(restaurantId);
        return dto;
    }

    /**
     * Crea un PlateUpdateRequestDto válido.
     */
    public static PlateUpdateRequestDto createValidPlateUpdateRequest() {
        PlateUpdateRequestDto dto = new PlateUpdateRequestDto();
        dto.setPrice(18000);
        dto.setDescription("Updated classic pizza with premium ingredients");
        return dto;
    }

    /**
     * Crea un PlateStatusUpdateRequestDto.
     */
    public static PlateStatusUpdateRequestDto createPlateStatusUpdateRequest(boolean active) {
        PlateStatusUpdateRequestDto dto = new PlateStatusUpdateRequestDto();
        dto.setActive(active);
        return dto;
    }

    // ==================== RESPONSE DTOs ====================

    /**
     * Crea un RestaurantResponseDto válido.
     */
    public static RestaurantResponseDto createValidRestaurantResponse() {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(1L);
        dto.setName("Pizza Palace");
        dto.setNit("123456789");
        dto.setAddress("123 Main St");
        dto.setPhone("+1234567890");
        dto.setLogoUrl("https://logo.example.com/pizza.png");
        dto.setOwnerId(100L);
        return dto;
    }

    /**
     * Crea un RestaurantListItemDto válido.
     */
    public static RestaurantListItemDto createValidRestaurantListItem() {
        RestaurantListItemDto dto = new RestaurantListItemDto();
        dto.setName("Pizza Palace");
        dto.setLogoUrl("https://logo.example.com/pizza.png");
        return dto;
    }

    /**
     * Crea múltiples RestaurantListItemDto para pruebas de listado.
     */
    public static List<RestaurantListItemDto> createRestaurantListItems() {
        RestaurantListItemDto item1 = new RestaurantListItemDto();
        item1.setName("Pizza Palace");
        item1.setLogoUrl("https://logo.example.com/pizza.png");

        RestaurantListItemDto item2 = new RestaurantListItemDto();
        item2.setName("Burger House");
        item2.setLogoUrl("https://logo.example.com/burger.png");

        return List.of(item1, item2);
    }

    /**
     * Crea un PlateResponseDto válido.
     */
    public static PlateResponseDto createValidPlateResponse() {
        PlateResponseDto dto = new PlateResponseDto();
        dto.setId(1L);
        dto.setName("Margherita Pizza");
        dto.setPrice(15000);
        dto.setDescription("Classic pizza with tomato sauce and mozzarella cheese");
        dto.setImageUrl("https://images.example.com/margherita.jpg");
        dto.setCategory(PlateCategory.PRINCIPAL);
        dto.setActive(true);
        dto.setRestaurantId(1L);
        return dto;
    }

    /**
     * Crea múltiples PlateResponseDto para pruebas de listado.
     */
    public static List<PlateResponseDto> createPlateResponseList() {
        PlateResponseDto dto1 = new PlateResponseDto();
        dto1.setId(1L);
        dto1.setName("Caesar Salad");
        dto1.setPrice(8000);
        dto1.setDescription("Fresh lettuce with Caesar dressing");
        dto1.setImageUrl("https://images.example.com/caesar.jpg");
        dto1.setCategory(PlateCategory.ENTRADA);
        dto1.setActive(true);
        dto1.setRestaurantId(1L);

        PlateResponseDto dto2 = new PlateResponseDto();
        dto2.setId(2L);
        dto2.setName("Margherita Pizza");
        dto2.setPrice(15000);
        dto2.setDescription("Classic pizza with tomato sauce");
        dto2.setImageUrl("https://images.example.com/margherita.jpg");
        dto2.setCategory(PlateCategory.PRINCIPAL);
        dto2.setActive(true);
        dto2.setRestaurantId(1L);

        return List.of(dto1, dto2);
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Crea un RestaurantModel con nombre inválido (solo números).
     */
    public static RestaurantModel createInvalidNameRestaurantModel() {
        return createRestaurantModel(1L, "123456", "123456789", "123 Main St",
                "+1234567890", "https://logo.example.com/test.png", 100L);
    }

    /**
     * Crea un RestaurantModel con teléfono inválido.
     */
    public static RestaurantModel createInvalidPhoneRestaurantModel() {
        return createRestaurantModel(1L, "Pizza Palace", "123456789", "123 Main St",
                "invalid-phone", "https://logo.example.com/test.png", 100L);
    }

    /**
     * Crea un PlateModel con precio inválido (negativo).
     */
    public static PlateModel createInvalidPricePlateModel() {
        return createPlateModel(1L, "Test Plate", -100, "Test description",
                "https://images.example.com/test.jpg", PlateCategory.PRINCIPAL, true, 1L);
    }

    /**
     * Crea un PlateModel con descripción vacía.
     */
    public static PlateModel createEmptyDescriptionPlateModel() {
        return createPlateModel(1L, "Test Plate", 10000, "",
                "https://images.example.com/test.jpg", PlateCategory.PRINCIPAL, true, 1L);
    }

    /**
     * Crea constantes útiles para las pruebas.
     */
    public static class Constants {
        public static final Long VALID_OWNER_ID = 100L;
        public static final Long INVALID_OWNER_ID = 999L;
        public static final Long VALID_RESTAURANT_ID = 1L;
        public static final Long INVALID_RESTAURANT_ID = 999L;
        public static final Long VALID_PLATE_ID = 1L;
        public static final Long INVALID_PLATE_ID = 999L;

        public static final String VALID_NIT = "123456789";
        public static final String DUPLICATE_NIT = "987654321";
        public static final String VALID_PHONE = "+1234567890";
        public static final String INVALID_PHONE = "invalid-phone";

        public static final String VALID_PLATE_NAME = "Test Plate";
        public static final String DUPLICATE_PLATE_NAME = "Existing Plate";
    }
}
