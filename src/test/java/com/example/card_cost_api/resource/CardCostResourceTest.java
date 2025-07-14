package com.example.card_cost_api.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/import.sql")
public class CardCostResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    private String getJwtToken() throws Exception {
        String loginPayload = """
            {
                "username": "user1",
                "password": "userPass1"
            }
        """;

        MvcResult loginResult = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload)
        ).andExpect(status().isOk()).andReturn();

        Map<String, String> response = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        return response.get("token");
    }

    @Test
    @Transactional
    public void testGetCardRelatedCost() throws Exception {
        String payload = """
            {"cardNumber":"45717360"}
        """;

        Map<String, Object> binlistResponse = Map.of(
                "country", Map.of("alpha2", "DK")
        );

        when(restTemplate.exchange(
                eq("https://lookup.binlist.net/45717360"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(binlistResponse, HttpStatus.OK));

        String token = getJwtToken();

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/given-card-related-cost")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(json, new TypeReference<>() {});

        Assertions.assertEquals("DK", response.get("country"));
        Assertions.assertEquals(12, response.get("cost"));
    }

    @Test
    public void testMissingCardNumber() throws Exception {
        String payload = "{}";
        String token = getJwtToken();

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/given-card-related-cost")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Map<String, String> response = objectMapper.readValue(json, new TypeReference<>() {});

        Assertions.assertEquals("cardNumber is required", response.get("error"));
    }

    @Test
    public void testInvalidCardNumberFromBinlist() throws Exception {
        String payload = """
            {"cardNumber":"00000000"}
        """;

        when(restTemplate.exchange(
                eq("https://lookup.binlist.net/00000000"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenThrow(HttpClientErrorException.BadRequest.class);

        String token = getJwtToken();

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/given-card-related-cost")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Map<String, String> response = objectMapper.readValue(json, new TypeReference<>() {});

        Assertions.assertEquals("Invalid card number", response.get("error"));
    }
}
