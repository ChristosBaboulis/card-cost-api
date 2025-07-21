package com.example.card_cost_api.service;

import com.example.card_cost_api.domain.ClearingCost;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest
@Sql("/import.sql")
public class ClearingCostServiceTest {
    @Autowired
    private ClearingCostService clearingService;

    @Test
    @Transactional
    public void testGetAll() {
        List<ClearingCost> ccs = clearingService.getAll();
        Assertions.assertEquals(7, ccs.size());
    }

    @Test
    @Transactional
    public void testGetByCountryCode() {
        Optional<ClearingCost> cc = clearingService.getByCountryCode("US");
        Assertions.assertTrue(cc.isPresent());
        Assertions.assertEquals("US", cc.get().getCountryCode());
    }

    @Test
    @Transactional
    public void testSave(){
        ClearingCost cc = new ClearingCost("AU", 15);
        ClearingCost saved = clearingService.save(cc);
        Assertions.assertEquals("AU", saved.getCountryCode());
        Assertions.assertEquals(Integer.valueOf(15), saved.getCost());
    }

    @Test
    @Transactional
    public void testDeleteById(){
        List<ClearingCost> ccs = clearingService.getAll();
        clearingService.deleteById(ccs.get(0).getId());
        ccs = clearingService.getAll();
        Assertions.assertEquals(6, clearingService.getAll().size());
    }

    @Test
    @Transactional
    public void testGetById(){
        List<ClearingCost> ccs = clearingService.getAll();
        Optional<ClearingCost> cc = clearingService.getById(ccs.getFirst().getId());
        Assertions.assertTrue(cc.isPresent());
        Assertions.assertEquals("US", cc.get().getCountryCode());
    }
}
