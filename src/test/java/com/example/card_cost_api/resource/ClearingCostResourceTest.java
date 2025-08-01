package com.example.card_cost_api.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.example.card_cost_api.representation.ClearingCostRepresentation;
import com.example.card_cost_api.service.ClearingCostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/import.sql")
public class ClearingCostResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClearingCostService clearingCostService;

    private String getToken() throws Exception {
        String loginRequest = """
            {
              "username": "admin1",
              "password": "adminPass"
            }
            """;

        MvcResult result = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Map<String, String> response = objectMapper.readValue(json, new TypeReference<>() {});
        return response.get("token");
    }

    @Test
    @Transactional
    public void testGetAll() throws Exception {
        String token = getToken();

        MvcResult mvcResult = mockMvc.perform(
                        get("/api/clearing-costs")
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<ClearingCostRepresentation> reps = objectMapper.readValue(json, new TypeReference<>() {});
        Assertions.assertFalse(reps.isEmpty());
        Assertions.assertEquals("US", reps.getFirst().countryCode);
    }

    @Test
    @Transactional
    public void testGetById() throws Exception {
        String token = getToken();

        MvcResult mvcResult = mockMvc.perform(
                        get("/api/clearing-costs/4002")
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        ClearingCostRepresentation t = objectMapper.readValue(json, ClearingCostRepresentation.class);

        Assertions.assertEquals("GR", t.countryCode);
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {
        String token = getToken();

        ClearingCostRepresentation rep = new ClearingCostRepresentation();
        rep.countryCode = "AU";
        rep.cost = 12;

        String requestBody = objectMapper.writeValueAsString(rep);

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/clearing-costs")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        ClearingCostRepresentation t = objectMapper.readValue(json, ClearingCostRepresentation.class);

        Assertions.assertEquals("AU", t.countryCode);
        Assertions.assertEquals(12, t.cost);
    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        String token = getToken();

        ClearingCostRepresentation rep = new ClearingCostRepresentation();
        rep.countryCode = "AU";
        rep.cost = 12;

        String requestBody = objectMapper.writeValueAsString(rep);

        MvcResult mvcResult = mockMvc.perform(
                        put("/api/clearing-costs/4002")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        ClearingCostRepresentation t = objectMapper.readValue(json, ClearingCostRepresentation.class);

        Assertions.assertEquals("AU", t.countryCode);
        Assertions.assertEquals(12, t.cost);
    }

    @Test
    @Transactional
    public void testDelete() throws Exception {
        String token = getToken();

        mockMvc.perform(
                        delete("/api/clearing-costs/4002")
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(6, clearingCostService.getAll().size());
    }
}
