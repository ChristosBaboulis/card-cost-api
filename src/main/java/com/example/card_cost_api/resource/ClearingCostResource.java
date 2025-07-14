package com.example.card_cost_api.resource;

import com.example.card_cost_api.domain.ClearingCost;
import com.example.card_cost_api.representation.ClearingCostMapper;
import com.example.card_cost_api.representation.ClearingCostRepresentation;
import com.example.card_cost_api.service.ClearingCostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clearing-costs")
public class ClearingCostResource {
    @Autowired
    private ClearingCostService clearingService;

    @Autowired
    private ClearingCostMapper mapper;

    @GetMapping
    public ResponseEntity<List<ClearingCostRepresentation>> getAll() {
        return ResponseEntity.ok(mapper.toRepresentationList(clearingService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClearingCostRepresentation> getById(@PathVariable Long id) {
        return clearingService.getById(id)
                .map(mapper::toRepresentation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClearingCostRepresentation> create(@Valid @RequestBody ClearingCostRepresentation rep) {
        ClearingCost saved = clearingService.save(mapper.toModel(rep));
        return ResponseEntity
                .created(URI.create("/api/clearing-costs/" + saved.getId()))
                .body(mapper.toRepresentation(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClearingCostRepresentation> update(@PathVariable Long id, @Valid @RequestBody ClearingCostRepresentation rep) {
        ClearingCost updated = clearingService.upsertByCountryCode(mapper.toModel(rep));
        return ResponseEntity.ok(mapper.toRepresentation(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (clearingService.getById(id).isPresent()) {
            clearingService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
