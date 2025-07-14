package com.example.card_cost_api.service;

import com.example.card_cost_api.domain.ClearingCost;
import com.example.card_cost_api.persistence.ClearingCostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClearingCostService {
    private final ClearingCostRepository repository;

    public ClearingCostService(ClearingCostRepository repository) {
        this.repository = repository;
    }

    public List<ClearingCost> getAll() {
        return repository.findAll();
    }

    public Optional<ClearingCost> getByCountryCode(String countryCode) {
        return repository.findByCountryCode(countryCode.toUpperCase());
    }

    public ClearingCost save(ClearingCost clearingCost) {
        clearingCost.setCountryCode(clearingCost.getCountryCode().toUpperCase());
        return repository.save(clearingCost);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<ClearingCost> getById(Long id) {
        return repository.findById(id);
    }

    public ClearingCost upsertByCountryCode(ClearingCost incoming) {
        return repository.findByCountryCode(incoming.getCountryCode().toUpperCase())
                .map(existing -> {
                    existing.setCost(incoming.getCost());
                    return repository.save(existing);
                })
                .orElseGet(() -> repository.save(incoming));
    }
}
