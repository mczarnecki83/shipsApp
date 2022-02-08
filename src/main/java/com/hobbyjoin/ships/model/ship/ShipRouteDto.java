package com.hobbyjoin.ships.model.ship;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ShipRouteDto {
    @JsonView({ShipRouteView.Normal.class})
    private String name;

    @JsonView({ShipRouteView.Normal.class})
    private Integer mmsi;

    @JsonView({ShipRouteView.Map.class})
    private Double latitude;

    @JsonView({ShipRouteView.Map.class})
    private Double longitude;

    @JsonView({ShipRouteView.Tax.class})
    private LocalDate localDate;

    @JsonView({ShipRouteView.Map.class})
    private Double actualCourse;

    @JsonView({ShipRouteView.Map.class, ShipRouteView.Tax.class})
    private Integer shipType;

    @JsonView({ShipRouteView.Map.class, ShipRouteView.Tax.class})
    private String destination;

    @JsonView({ShipRouteView.Map.class, ShipRouteView.Tax.class})
    private Double destinationLatitude;

    @JsonView({ShipRouteView.Map.class, ShipRouteView.Tax.class})
    private Double destinationLongitude;

    @JsonView({ShipRouteView.Map.class, ShipRouteView.Tax.class})
    private LocalDateTime arrivalTime;

    @JsonView({ShipRouteView.Map.class})
    private Double actualSpeed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) { this.mmsi = mmsi;}

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

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Double getActualCourse() {
        return actualCourse;
    }

    public void setActualCourse(Double actualCourse) {
        this.actualCourse = actualCourse;
    }

    public Integer getShipType() {
        return shipType;
    }

    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getActualSpeed() {
        return actualSpeed;
    }

    public void setActualSpeed(Double actualSpeed) {
        this.actualSpeed = actualSpeed;
    }

    public Double getDestinationLatitude() {return destinationLatitude;}

    public void setDestinationLatitude(Double destinationLatitude) {this.destinationLatitude = destinationLatitude;}

    public Double getDestinationLongitude() {return destinationLongitude;}

    public void setDestinationLongitude(Double destinationLongitude) {this.destinationLongitude = destinationLongitude;}

    @Override
    public String toString() {
        return "ShipRouteDto{" +
                "name='" + name + '\'' +
                ", mmsi=" + mmsi +
                '}';
    }
}
