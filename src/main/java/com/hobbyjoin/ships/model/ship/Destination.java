package com.hobbyjoin.ships.model.ship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message="field_cannot_by_empty")
    @Size(min=3, max = 255, message = "name_length")
    private String name;

    @Valid
    @Range(min=-180, max = 180, message = "the_latitude_must_be_between")
    private Double latitude;

    @Valid
    @Range(min=-180, max = 180, message = "the_longitude_must_be_between")
    private Double longitude;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String info;

    private Boolean blackList = false;

    @Column(nullable = true)
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getBlackList() {
        return blackList;
    }

    public void setBlackList(Boolean blackList) {
        this.blackList = blackList;
    }

    @OneToMany(mappedBy="destination")
    @BatchSize(size=16)
    @JsonIgnore
    private List<Route> routes = new ArrayList<>();

    public void addRoute(Route route) {
        routes.add(route);
        route.setDestination(this);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public Destination() {
    }

    public Destination(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
