package com.hobbyjoin.ships.model.ship;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="ship_mmsi",referencedColumnName="mmsi")
    private Ship ship;

    @ManyToOne
    @JoinColumn(name="destination_id",referencedColumnName="id")
    private Destination destination;

    private Double actualCourse;
    private Double actualSpeed;
    private LocalDateTime addedTime;
    private Double latitude;
    private Double longitude;
    private LocalDateTime arrivalTime;


    public Route() {}

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public LocalDateTime getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(LocalDateTime addedTime) {
        this.addedTime = addedTime;
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

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getActualCourse() {
        return actualCourse;
    }

    public void setActualCourse(Double actualCourse) {
        this.actualCourse = actualCourse;
    }

    public Double getActualSpeed() {
        return actualSpeed;
    }

    public void setActualSpeed(Double actualSpeed) {
        this.actualSpeed = actualSpeed;
    }
}
