package com.example.card_cost_api.representation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class ClearingCostRepresentation {
    public Long id;

    @NotBlank
    public String countryCode;

    @NotNull
    @Min(0)
    public Integer cost;
}
