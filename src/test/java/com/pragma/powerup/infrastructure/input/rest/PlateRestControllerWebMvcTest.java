package com.pragma.powerup.infrastructure.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PlateRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(PlateRestControllerWebMvcTest.TestConfig.class)
class PlateRestControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IPlateHandler plateHandler;

    @Test
    @DisplayName("POST /api/v1/plates should return 201 Created when valid")
    void shouldCreatePlate() throws Exception {
        PlateCreateRequestDto request = new PlateCreateRequestDto();
        request.setName("A");
        request.setPrice(10);
        request.setDescription("d");
        request.setImageUrl("http://img");
        request.setCategory(com.pragma.powerup.domain.model.PlateCategory.ENTRADA);
        request.setRestaurantId(5L);

        PlateResponseDto response = new PlateResponseDto();
        response.setId(1L);
        response.setName("A");
        response.setPrice(10);
        response.setDescription("d");
        response.setImageUrl("http://img");
        response.setCategory(com.pragma.powerup.domain.model.PlateCategory.ENTRADA);
        response.setActive(true);
        response.setRestaurantId(5L);

        Mockito.when(plateHandler.create(any(), any())).thenReturn(response);

        mockMvc
                .perform(
                        post("/api/v1/plates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-User-Id", "10")
                                .header("X-User-Email", "owner@test.com")
                                .header("X-User-Role", "OWNER")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("PUT /api/v1/plates/{id} should return 200 OK when valid")
    void shouldUpdatePlate() throws Exception {
        var request = new com.pragma.powerup.application.dto.request.PlateUpdateRequestDto();
        request.setPrice(20);
        request.setDescription("new desc");

        PlateResponseDto response = new PlateResponseDto();
        response.setId(1L);
        response.setName("A");
        response.setPrice(20);
        response.setDescription("new desc");
        response.setImageUrl("http://img");
        response.setCategory(com.pragma.powerup.domain.model.PlateCategory.ENTRADA);
        response.setActive(true);
        response.setRestaurantId(5L);

        Mockito.when(plateHandler.update(any(), any(), any())).thenReturn(response);

        mockMvc
                .perform(
                        put("/api/v1/plates/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-User-Id", "10")
                                .header("X-User-Email", "owner@test.com")
                                .header("X-User-Role", "OWNER")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/v1/plates/{id}/status should return 200 OK when valid")
    void shouldUpdatePlateStatus() throws Exception {
        var request = new com.pragma.powerup.application.dto.request.PlateStatusUpdateRequestDto();
        request.setActive(false);

        PlateResponseDto response = new PlateResponseDto();
        response.setId(1L);
        response.setName("A");
        response.setPrice(20);
        response.setDescription("new desc");
        response.setImageUrl("http://img");
        response.setCategory(com.pragma.powerup.domain.model.PlateCategory.ENTRADA);
        response.setActive(false);
        response.setRestaurantId(5L);

        Mockito.when(plateHandler.updateStatus(any(), any(), any())).thenReturn(response);

        mockMvc
                .perform(
                        patch("/api/v1/plates/{id}/status", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-User-Id", "10")
                                .header("X-User-Email", "owner@test.com")
                                .header("X-User-Role", "OWNER")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        IPlateHandler plateHandler() {
            return Mockito.mock(IPlateHandler.class);
        }
    }

    @Test
    @DisplayName("GET /api/v1/plates/restaurant/{id} should return 200 OK")
    void shouldListPlatesByRestaurant() throws Exception {
        PlateResponseDto p = new PlateResponseDto();
        p.setId(1L);
        p.setName("A");
        p.setPrice(10);
        p.setDescription("d");
        p.setImageUrl("http://img");
        p.setCategory(com.pragma.powerup.domain.model.PlateCategory.ENTRADA);
        p.setActive(true);
        p.setRestaurantId(5L);

        Mockito.when(
                plateHandler.listByRestaurant(
                        Mockito.anyLong(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(java.util.List.of(p));

        mockMvc.perform(get("/api/v1/plates/restaurant/{id}", 5)).andExpect(status().isOk());
    }
}
