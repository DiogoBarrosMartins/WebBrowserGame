package com.superapi.gamerealm.model.buildings;

import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Construction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long buildingId;
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

    public Construction(int buildingId, Village village, LocalDateTime startedAt, LocalDateTime endsAt) {
        this.buildingId = buildingId;
        this.village = village;
        this.startedAt = startedAt;
        this.endsAt = endsAt;
    }

    public Construction() {
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
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

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    @Override
public String toString() {
    return "Construction{" +
            "id=" + id +
            ", buildingId=" + buildingId +
            ", village=" + village +
            ", startedAt=" + startedAt +
            ", endsAt=" + endsAt +
            '}';
}

}
