package com.superapi.gamerealm.model.resources;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class ResourceAmount {
    @Column(name = "amount") // Specify the column name for the 'amount' field
    private BigDecimal amount; // Use BigDecimal for precision with fractional amounts


    // Constructors, getters, and setters
}