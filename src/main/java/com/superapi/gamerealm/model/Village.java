package com.superapi.gamerealm.model;
import jakarta.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "village_type")
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int xCoordinate;

    @ManyToOne
    @JoinColumn(name = "grid_id")
    private Grid grid;
    @Column(nullable = false)
    private int yCoordinate;

    @ElementCollection
    @CollectionTable(name = "village_resources")
    @MapKeyColumn(name = "resource_type")
    @Column(name = "amount")
    private Map<String, Long> resources = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "village_buildings")
    @MapKeyColumn(name = "building_type")
    @Column(name = "level")
    private Map<String, Integer> buildings = new HashMap<>();


    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;



    public Village() {
    this.name ="default name";
    }

    private void initializeDefaultResources() {
        // Set default resource amounts
        resources.put("wheat", 1000L);
        resources.put("wood", 1000L);
        resources.put("stone", 500L);
        resources.put("gold", 500L);
    }

    private void initializeDefaultBuildings() {
        // Set default building levels
        buildings.put("farms", 0);
        buildings.put("lumbers", 0);
        buildings.put("rockMines", 0);
        buildings.put("goldMines", 0);
    }

    public void setCoordinates(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public Date getLastUpdateTime() {
        return lastUpdated;
    }

    public Map<String, Integer> getBuildings() {
        return buildings;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account; }

    public long getId() {
        return id;}

    public String getName() {
        return name; }

    public int getXCoordinate() {return xCoordinate;
    }

    public int getYCoordinate() {return yCoordinate;
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

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Map<String, Long> getResources() {
        return resources;
    }

    public void setResources(Map<String, Long> resources) {
        this.resources = resources;
    }

    public void setBuildings(Map<String, Integer> buildings) {
        this.buildings = buildings;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setXCoordinate(int i) {
        this.xCoordinate = i; }

    public void setYCoordinate(int i) {
        this.yCoordinate =i; }


}