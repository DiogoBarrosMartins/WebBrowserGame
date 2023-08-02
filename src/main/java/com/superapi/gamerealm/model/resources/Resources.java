package com.superapi.gamerealm.model.resources;

import com.superapi.gamerealm.model.Village;
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

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;


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
        return this.type == type ? amount : BigDecimal.ZERO;
    }

    public void setAmount(TypeOfResource type, BigDecimal amount) {
        if (this.type == type) {
            this.amount = amount;
        }
    }

    public void setVillage(Village village) {
   this.village = village;
    }

    public void setType(TypeOfResource type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TypeOfResource getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Village getVillage() {
        return village;
    }

    public void increaseAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    public void decreaseAmount(BigDecimal amount) {
        if (this.amount.compareTo(amount) >= 0) {
            this.amount = this.amount.subtract(amount);
        } else {
            throw new IllegalArgumentException("Not enough resources");
        }
    }
}








