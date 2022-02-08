package com.hobbyjoin.ships.port.WeatherOpenWeatherMap;

import com.hobbyjoin.ships.model.ship.DestinationRepository;
import com.hobbyjoin.ships.model.weather.WeatherApi;
import com.hobbyjoin.ships.model.weather.WeatherMini;
import com.hobbyjoin.ships.port.WeatherOpenWeatherMap.PrivateModel.OpenWeatherMap;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class WeatherOpenWeatherMapApiImpl implements WeatherApi {
    DestinationRepository destinationRepository;
    public WeatherOpenWeatherMapApiImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }
    private String apiKey = null;
    public WeatherOpenWeatherMapApiImpl() {
    }

    @Override
    public  WeatherMini getWeatherApi(Double latitude, Double longitude) {
        if(latitude==null || longitude==null){
            return null;
        }

        String weatherUrl = "http://api.openweathermap.org/data/2.5/onecall?lat="+latitude+"&lon="+longitude+"&appid=2083a79cf3370f5c0cde2c0b5ca5933c&exclude=minutely,hourly,daily,alerts&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Host", "http://localhost:8080");


        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<OpenWeatherMap> exchange = restTemplate.exchange(weatherUrl,HttpMethod.GET,httpEntity,OpenWeatherMap.class);

        WeatherMini weather = new WeatherMini();
        weather.setClouds(exchange.getBody().getCurrent().getClouds());
        weather.setTemp(exchange.getBody().getCurrent().getTemp());
        weather.setWindSpeed(exchange.getBody().getCurrent().getWindSpeed());
        weather.setVisibility(exchange.getBody().getCurrent().getVisibility());
        weather.setWindDeg(exchange.getBody().getCurrent().getWindDeg());
        return weather;
    }

}