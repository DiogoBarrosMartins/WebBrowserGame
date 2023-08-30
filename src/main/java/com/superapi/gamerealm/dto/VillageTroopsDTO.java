package com.superapi.gamerealm.dto;

public class VillageTroopsDTO {
    private Long id;
    private String troopTypeName;
    private int quantity;

    // Constructors, getters, setters, etc.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTroopTypeName() {
        return troopTypeName;
    }

    public void setTroopTypeName(String troopTypeName) {
        this.troopTypeName = troopTypeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
