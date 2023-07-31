package com.superapi.gamerealm.model;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.resources.Resources;
import jakarta.persistence.*;

import java.util.Date;

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

    @OneToOne(cascade = CascadeType.ALL) // Cascade all operations (persist, update, delete) to the associated resource
    @JoinColumn(name = "resources_id") // Specify the foreign key column
    private Resources resources;


    public Village() {
        System.out.println("VILLAGE NO ARGS CONSTRUCTOR ");

    }


    public Village(Coordinates coordinates) {
        System.out.println("VILLAGE COORDINATE ARGS CONSTRUCTOR "+ coordinates.getX() + " " + coordinates.getY());
        this.coordinates = coordinates;
        this.name = "default name";
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

    public Grid getGrid() {
        return grid;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }
}