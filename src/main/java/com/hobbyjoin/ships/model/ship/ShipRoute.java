package com.hobbyjoin.ships.model.ship;
import java.time.LocalDateTime;

public class ShipRoute {

    private String name;
    private Integer mmsi;
    private Double latitude;
    private Double longitude;
    private LocalDateTime arrivalTime;
    private Double actualCourse;
    private Double actualSpeed;
    private Integer shipType;
    private String destination;
    private Double destinationLatitude;
    private Double destinationLongitude;

    private ShipRoute(Builder builder) {
        this.name = builder.name;
        this.mmsi = builder.mmsi;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.arrivalTime = builder.arrivalTime;
        this.actualCourse = builder.actualCourse;
        this.actualSpeed = builder.actualSpeed;
        this.shipType = builder.shipType;
        this.destination = builder.destination;
        this.destinationLatitude = builder.destinationLatitude;
        this.destinationLongitude = builder.destinationLongitude;
    }

    public static final class Builder {
        private String name;
        private Integer mmsi;
        private Double latitude;
        private Double longitude;
        private LocalDateTime arrivalTime;
        private Double actualCourse;
        private Double actualSpeed;
        private Integer shipType;
        private String destination;
        private Double destinationLatitude;
        private Double destinationLongitude;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder mmsi(Integer mmsi) {
            this.mmsi = mmsi;
            return this;
        }

        public Builder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder arrivalTime(LocalDateTime arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public Builder actualCourse(Double actualCourse) {
            this.actualCourse = actualCourse;
            return this;
        }

        public Builder actualSpeed(Double actualSpeed) {
            this.actualSpeed = actualSpeed;
            return this;
        }

        public Builder shipType(Integer shipType) {
            this.shipType = shipType;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder destinationLatitude(Double destinationLatitude) {
            this.destinationLatitude = destinationLatitude;
            return this;
        }

        public Builder destinationLongitude(Double destinationLongitude) {
            this.destinationLongitude = destinationLongitude;
            return this;
        }


        public ShipRoute build() {
            return new ShipRoute(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
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

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
}
