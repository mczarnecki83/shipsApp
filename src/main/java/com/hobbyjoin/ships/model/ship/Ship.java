package com.hobbyjoin.ships.model.ship;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@NamedQuery(name=Ship.QUERY_ALL, query="SELECT o FROM Ship o")
public class Ship {
    public static final String QUERY_ALL = "query.Ship.all";

    @Id
    @Column
    private Integer mmsi;

    @Column
    private String name;

    @Column
    private Integer shipType;


    @OneToMany(mappedBy="ship")
    @BatchSize(size=6)
    private List<Route> routes = new ArrayList<>();

    public void addRoute(Route route) {
        routes.add(route);
        route.setShip(this);
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public Ship()
    {

    }

    public Ship(Integer mmsi, String name, Integer shipType) {
        this.mmsi = mmsi;
        this.name = name;
        this.shipType = shipType;
    }

    @JsonIgnore
    public List<Route> getRoutes() {
        return routes;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShipType() {
        return shipType;
    }

    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }
}
