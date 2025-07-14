package com.example.card_cost_api.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClearingCostTest {
    ClearingCost clearingCost;
    @BeforeEach
    public void setUp() {
        clearingCost = new ClearingCost("US", 5);
    }

    @Test
    public void testGetterSetter() {
        Assertions.assertEquals("US", clearingCost.getCountryCode());
        Assertions.assertEquals(Integer.valueOf(5), clearingCost.getCost());
        clearingCost.setCountryCode("GR");
        clearingCost.setCost(15);
        Assertions.assertEquals("GR", clearingCost.getCountryCode());
        Assertions.assertEquals(Integer.valueOf(15), clearingCost.getCost());
    }
}
