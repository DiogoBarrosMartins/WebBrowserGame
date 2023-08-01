package com.superapi.gamerealm.model.resources;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOfResource type;

    @Column(nullable = false)
    private BigDecimal amount;

    public Resources() {
        this.amount = BigDecimal.ZERO;
    }

    public Resources(TypeOfResource type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getAmount(TypeOfResource type) {
        return this.type.equals(type) ? amount : BigDecimal.ZERO;
    }


    public void setAmount(TypeOfResource type, BigDecimal amount) {
        if (this.type.equals(type)) {
            this.amount = amount;
        }
    }
}







