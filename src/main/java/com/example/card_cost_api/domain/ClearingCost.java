package com.example.card_cost_api.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "clearing_costs")
public class ClearingCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "cost",nullable = false)
    private Integer cost;

    public ClearingCost() {}

    public ClearingCost(String countryCode, Integer cost) {
        this.countryCode = countryCode;
        this.cost = cost;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Integer getCost() {
        return cost;
    }

    // Setters
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
