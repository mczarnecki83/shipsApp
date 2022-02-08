package com.hobbyjoin.ships;

import com.hobbyjoin.ships.model.ship.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class ShipService {
    private final ShipRepository shipRepository;
    private final EntityManager entityManager;


    public ShipService(ShipRepository shipRepository, EntityManager entityManager) {
        this.shipRepository = shipRepository;
        this.entityManager = entityManager;
    }

    public Page<Ship> getAllShips(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Ship> pagedResult = shipRepository.findAll(paging);
        return pagedResult;
    }


    public List getCountRoutesNativeQuery(Integer pageNo, Integer pageSize) {
        if(null==pageNo || pageNo<=0){pageNo=1;}
        if(null==pageSize || pageSize<=0){pageNo=6;}
        String sqlLimit = " LIMIT " + (pageNo - 1) * pageSize + " , " + pageSize + "  ";
        String sqlShip = "select s.mmsi,s.name,(select count(id) from Route as r where r.ship_mmsi=s.mmsi) as meter from Ship as s order by meter desc "+sqlLimit;
        final Query nativeQuery = entityManager.createNativeQuery(sqlShip);
        final List shipList = nativeQuery.getResultList();
        return shipList;
    }

    public Object getCountRoutesObjNativeQuery() {
        String sqlCountShip = "select count(mmsi) as meter from Ship";
        final Query sqlCountShipNative = entityManager.createNativeQuery(sqlCountShip);
        return sqlCountShipNative.getResultList().get(0);
    }











     Optional<Ship> getShipInstance(Integer mmsi){
        Optional<Ship> foundShip = shipRepository.findById(mmsi);
        return foundShip;
    }




      Optional<Ship> addNewShip(ShipRouteDto shipRouteDto) {
        final Optional<Ship> ship = Optional.of(new Ship(shipRouteDto.getMmsi(), shipRouteDto.getName(), shipRouteDto.getShipType()));
        shipRepository.save(ship.get());
        return ship;
    }
}
