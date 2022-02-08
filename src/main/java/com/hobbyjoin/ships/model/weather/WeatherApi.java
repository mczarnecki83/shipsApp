package com.hobbyjoin.ships.model.weather;

public interface WeatherApi {
    WeatherMini getWeatherApi(Double latitude, Double longitude);
}
