package com.superapi.gamerealm.model;

import com.superapi.gamerealm.component.Coordinates;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Optional;

@Entity
public class Village {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Coordinates coordinates;
    private String name;

    @ManyToOne
    private Grid grid;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public Village() {
        this.name = "default name";
    }

    public Village(  int x, int y) {
        this.coordinates = new Coordinates(x, y);
        this.lastUpdated = new Date();
    }

    public Date getLastUpdateTime() {
        return lastUpdated;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public void setId(long l) {
        this.id = l;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setGrid(Grid grid) {
  this.grid =grid;
    }
}