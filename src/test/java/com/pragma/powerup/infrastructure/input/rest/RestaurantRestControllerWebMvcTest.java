package com.pragma.powerup.infrastructure.input.rest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.RestaurantCreateRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantListItemDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RestaurantRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantRestControllerWebMvcTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private IRestaurantHandler restaurantHandler;

  @TestConfiguration
  static class TestConfig {
    @Bean
    IRestaurantHandler restaurantHandler() {
      return Mockito.mock(IRestaurantHandler.class);
    }
  }

  @Test
  @DisplayName("GET /api/v1/restaurants returns 200")
  void listRestaurants_ok() throws Exception {
    var item = new RestaurantListItemDto();
    item.setName("A");
    item.setLogoUrl("http://logo.png");
    Mockito.when(restaurantHandler.list(anyInt(), anyInt())).thenReturn(List.of(item));

    mockMvc
        .perform(get("/api/v1/restaurants").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("POST /api/v1/restaurants returns 201 with Location header")
  void createRestaurant_created() throws Exception {
    RestaurantCreateRequestDto req = new RestaurantCreateRequestDto();
    req.setName("Pizza Planet");
    req.setNit("123456");
    req.setAddress("Main");
    req.setPhone("+573005698325");
    req.setLogoUrl("http://logo.png");
    req.setOwnerId(10L);

    RestaurantResponseDto resp = new RestaurantResponseDto();
    resp.setId(1L);
    resp.setName("Pizza Planet");
    Mockito.when(restaurantHandler.create(Mockito.any())).thenReturn(resp);

    mockMvc
        .perform(
            post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }
}
