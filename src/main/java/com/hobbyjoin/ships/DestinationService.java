package com.hobbyjoin.ships;

import com.hobbyjoin.ships.model.ship.*;
import com.hobbyjoin.ships.model.weather.WeatherMini;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {
    private final DestinationRepository destinationRepository;
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

     Optional<Destination> getDestinationInstance(String name){
        Optional<Destination> foundDestination = destinationRepository.findByName(name);
        return foundDestination;
    }

      Optional<Destination> addNewDestination(ShipRouteDto shipRouteDto) {
        final Optional<Destination> destination = Optional.of(new Destination(shipRouteDto.getDestination(), shipRouteDto.getDestinationLatitude(), shipRouteDto.getDestinationLongitude()));
           destinationRepository.save(destination.get());
          return destination;
    }

    public List<Destination> getDestinationsIfBlackListIs(Boolean blackList) {
        if(blackList==null){blackList=false;}
        final List<Destination> destinationl = destinationRepository.findByBlackList(blackList);
        return destinationl;
    }

    public Optional<Destination> getDestinationFromId(int id) {
        final Optional<Destination> destinationData = destinationRepository.findById(id);
        return destinationData;
    }

    public List<Destination> getAllDestinations() {
        final List<Destination> all = destinationRepository.findAll();
        return all;
    }

    public String prepareReturnAjaxWeatherMessage(WeatherMini weatherApi) {
        if(weatherApi.equals(null)){
            return "";
        }
        Translator translator = Translator.getInstance();
        translator.setTranslatorHandler(LocaleContextHolder.getLocale().toString());
        StringBuilder weatherDataView = new StringBuilder();
        weatherDataView.append("<br/>"+ translator.translate("weatherAjax.temperature")  +"  <b>"+weatherApi.getTemp()+"</b>&#x2103;");
        weatherDataView.append("<br/>"+ translator.translate("weatherAjax.clouds")  +"  <b>"+weatherApi.getClouds()+"</b>%");
        weatherDataView.append("<br/>"+ translator.translate("weatherAjax.visibility")+"  <b>"+weatherApi.getVisibility()+"</b>m");
        weatherDataView.append("<br/>"+ translator.translate("weatherAjax.wind_speed")+"  <b>"+weatherApi.getWindSpeed()+"</b> m/s");
        weatherDataView.append("<br/>"+ translator.translate("weatherAjax.wind_degress")+"  <b>"+weatherApi.getWindDeg()+"</b> (360&deg;)");
        return weatherDataView.toString();
    }

    public Destination saveDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    public String getValidationNameExistsError(String oldName, String newName) {
        String translatedError="";
        if(!oldName.equals(newName)){
            Optional<Destination> byName = destinationRepository.findByName(newName);
            if(!byName.isEmpty()){
                Translator translator = Translator.getInstance();
                translator.setTranslatorHandler(LocaleContextHolder.getLocale().toString());
                translatedError = translator.translate("error.new_name_already_exists_change_it");
            }
        }
            return translatedError;
    }

    public void deleteDestination(int id) {
        destinationRepository.deleteById(id);
    }

    public List<Destination> getMyDestinations(long id) {
        final List<Destination> destinations = destinationRepository.findByUserId(id);
        return destinations;
    }
}
