package com.hobbyjoin.ships;

import com.hobbyjoin.ships.model.ship.ShipRoute;
import com.hobbyjoin.ships.model.ship.ShipRouteApi;
import com.hobbyjoin.ships.model.ship.ShipRouteDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InputShipApiService {

    @ResponseBody
    List<ShipRouteDto> getShipsFromAreaFromApiAsShipDto(ShipRouteApi shipApi)
    {
        List<ShipRoute> shipsFromArea = getShipsFromAreaFromApi(shipApi);
        final List<ShipRouteDto> shipsFromAreaDto = mapShipToShipDto(shipsFromArea);
        return shipsFromAreaDto;
    }

    private List<ShipRoute> getShipsFromAreaFromApi(ShipRouteApi shipApi){
        final List<ShipRoute> ships = shipApi.getShips();
        return ships;
    }

    private List<ShipRouteDto> mapShipToShipDto(List<ShipRoute> shipsFromArea){
        List<ShipRouteDto> shipDtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<ShipRoute, ShipRouteDto>() {
            @Override
            protected void configure() {
                map().setName(source.getName());
                map().setMmsi(source.getMmsi());
                map().setLatitude(source.getLatitude());
                map().setLongitude(source.getLongitude());
                map().setLocalDate(LocalDate.now());
                map().setActualCourse(source.getActualCourse());
                map().setActualSpeed(source.getActualSpeed());
                map().setShipType(source.getShipType());
                map().setDestination(source.getDestination());
                map().setArrivalTime(source.getArrivalTime());
                map().setDestinationLatitude(source.getDestinationLatitude());
                map().setDestinationLongitude(source.getDestinationLongitude());
            }
        });
        for (ShipRoute singleShip: shipsFromArea)
        {
            ShipRoute shipToAdd = new ShipRoute.Builder()
                    .name(singleShip.getName())
                    .mmsi(singleShip.getMmsi())
                    .latitude(singleShip.getLatitude())
                    .longitude(singleShip.getLongitude())
                    .actualCourse(singleShip.getActualCourse())
                    .actualSpeed(singleShip.getActualSpeed())
                    .shipType(singleShip.getShipType())
                    .destination(singleShip.getDestination())
                    .arrivalTime(singleShip.getArrivalTime())
                    .destinationLatitude(singleShip.getDestinationLatitude())
                    .destinationLongitude(singleShip.getDestinationLongitude())
                    .build();
            shipDtoList.add(modelMapper.map(shipToAdd, ShipRouteDto.class));
        }
        return shipDtoList;
    }


}
