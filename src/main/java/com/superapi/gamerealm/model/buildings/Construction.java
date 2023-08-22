package com.superapi.gamerealm.model.buildings;

import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Construction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Building building;
    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endsAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }
}
