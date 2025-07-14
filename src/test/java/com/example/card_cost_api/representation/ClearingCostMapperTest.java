package com.example.card_cost_api.representation;

import com.example.card_cost_api.domain.ClearingCost;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClearingCostMapperTest {
    @Autowired
    private ClearingCostMapper mapper;

    @Test
    public void testMapper() {
        ClearingCostRepresentation rep = new ClearingCostRepresentation();
        rep.id = 1L;
        rep.countryCode = "US";
        rep.cost = 5;

        ClearingCost entity = mapper.toModel(rep);

        Assertions.assertEquals("US", entity.getCountryCode());
        Assertions.assertEquals(Integer.valueOf(5), entity.getCost());

        ClearingCostRepresentation rep2 = mapper.toRepresentation(entity);
        Assertions.assertEquals(rep.countryCode, rep2.countryCode);
        Assertions.assertEquals(rep.cost, rep2.cost);
    }
}
