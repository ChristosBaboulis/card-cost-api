package com.example.card_cost_api.resource;

import com.example.card_cost_api.domain.ClearingCost;
import com.example.card_cost_api.service.ClearingCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/given-card-related-cost")
public class CardCostResource {
    private static final Logger logger = LoggerFactory.getLogger(CardCostResource.class);

    @Autowired
    private ClearingCostService clearingService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<?> getCardRelatedCost(@RequestBody Map<String, String> payload) {
        String cardNumber = payload.get("cardNumber");

        if (cardNumber == null || cardNumber.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "cardNumber is required"));
        }

        String url = "https://lookup.binlist.net/" + cardNumber;

        Map<String, Object> response;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept-Version", "3");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            logger.info("Sending request to Binlist for cardNumber: {}", cardNumber);

            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            logger.info("Received response from Binlist: {}", responseEntity.getBody());

            response = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error when calling Binlist", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid card number"));
        }

        Map<String, String> countryMap = (Map<String, String>) response.get("country");
        String countryCode = countryMap != null ? countryMap.get("alpha2") : null;

        if (countryCode == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Country not found"));
        }

        ClearingCost costEntry = clearingService.getByCountryCode(countryCode).orElse(
                clearingService.getByCountryCode("OTHERS").orElse(null)
        );

        if (costEntry == null) {
            return ResponseEntity.status(500).body(Map.of("error", "No cost configuration found"));
        }

        int cost = costEntry.getCost();

        return ResponseEntity.ok(Map.of(
                "country", countryCode,
                "cost", cost
        ));
    }
}
