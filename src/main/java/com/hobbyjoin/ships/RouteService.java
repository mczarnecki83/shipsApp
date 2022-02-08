package com.hobbyjoin.ships;

import com.hobbyjoin.ships.model.ship.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RouteService {
    private final EntityManager entityManager;
    private static ShipRepository shipRepository;
    private static RouteRepository routeRepository;
    private static DestinationService destinationService;
    private static DestinationRepository destinationRepository;
    private final ShipService shipService;

    public RouteService(DestinationRepository destinationRepository, ShipRepository shipRepository, RouteRepository routeRepository, ShipService shipService, EntityManager entityManager, DestinationService destinationService) {
        this.shipRepository = shipRepository;
        this.routeRepository = routeRepository;
        this.shipService = shipService;
        this.entityManager = entityManager;
        this.destinationService = destinationService;
        this.destinationRepository = destinationRepository;
    }

    @Transactional
    public void clearStandingShips() {
        String sqlShip = "delete from Route where ship_mmsi in (select distinct(z.ship_mmsi) from Route as z where (select max(w.latitude)-min(w.latitude) as difference from Route as w where w.ship_mmsi=z.ship_mmsi) <0.01 and (select count(w2.latitude) from Route as w2 where w2.ship_mmsi=z.ship_mmsi) >4)";
        final Query nativeQuery = entityManager.createNativeQuery(sqlShip);
        final int i = nativeQuery.executeUpdate();

    }

    @Transactional
    public void proceedingsSingleShipsDtoEntry(ShipRouteDto singleShip) {
        final Optional<Ship> shipInstance = shipService.getShipInstance(singleShip.getMmsi());
        Optional<Ship> actualShip = null;
        if (shipInstance.isEmpty()) {
            actualShip = shipService.addNewShip(singleShip);
        } else {
            actualShip = shipInstance;
        }

        final Optional<Destination> destinationInstance = destinationService.getDestinationInstance(singleShip.getDestination());
        Optional<Destination> actualDestination = null;

        if (!destinationInstance.isEmpty()) {
            if (null != destinationInstance.get().getLatitude()) {
                actualDestination = destinationInstance;
            }
        }
        addRouteToShip(singleShip, actualShip, actualDestination);
    }

    public List<Route> getRoutesFromMmsi(int mmsi) {
        final List<Route> routeListFromMmsi = routeRepository.findByShip_Mmsi(mmsi);
        return routeListFromMmsi;
    }

    private void addRouteToShip(ShipRouteDto ShipFromDto, Optional<Ship> actualShip, Optional<Destination> actualDestination) {
        final Route route = addRoute(ShipFromDto);
        actualShip.get().addRoute(route);
        shipRepository.save(actualShip.get());

        if (actualDestination != null) {
            actualDestination.get().addRoute(route);
            destinationRepository.save(actualDestination.get());
        }
        routeRepository.save(route);
    }

    Route addRoute(ShipRouteDto singleShip) {
        final Route route = new Route();
        route.setActualCourse(singleShip.getActualCourse());
        route.setActualSpeed(singleShip.getActualSpeed());
        route.setArrivalTime(singleShip.getArrivalTime());
        route.setLatitude(singleShip.getLatitude());
        route.setLongitude(singleShip.getLongitude());
        route.setAddedTime(LocalDateTime.now());
        return route;
    }

     List<Route> getAllShipsRoutes() {
        List<Route> allByAddedTimeBetween = routeRepository.findAll();
        return allByAddedTimeBetween;
    }


    public String getDate(String date) {
        String newDate="";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            newDate = LocalDate.parse(date, formatter).toString();
        }
        catch(Exception e){
            newDate = LocalDate.now().toString();
        }
        return newDate;
    }

    public List getAllRoutesByDate(String dateToSearch) {
        String dateFrom = dateToSearch+" 00:00:00";
        String dateTo = dateToSearch+" 23:59:59";
        //String sqlShip = "SELECT distinct(ship_mmsi), id, added_time, latitude, longitude FROM Route WHERE added_time between :dateFrom and :dateTo order by added_time";
        String sqlShip = "SELECT ship_mmsi,count(id), latitude,longitude FROM Route WHERE added_time between :dateFrom and :dateTo group by ship_mmsi";

        final Query nativeQuery = entityManager.createNativeQuery(sqlShip);
        nativeQuery.setParameter("dateFrom",dateFrom);
        nativeQuery.setParameter("dateTo",dateTo);
        final List routes = nativeQuery.getResultList();
        return routes;
    }


    public List getPopularRoutesByDateNative(String dateToSearch) {
        String dateFrom = dateToSearch+" 00:00:00";
        String dateTo = dateToSearch+" 23:59:59";
        List<Object[]> routeList = null;
        final Query nativeQuery =entityManager.createNativeQuery("select max(latitude)-min(latitude) as differenceLati,  max(longitude)-min(longitude) as differenceLong ,count(latitude) as meterLati, ship_mmsi from Route WHERE added_time between :dateFrom and :dateTo  group by ship_mmsi having differenceLati>=0.2 or differenceLong>=0.2 limit 20");
        nativeQuery.setParameter("dateFrom",dateFrom);
        nativeQuery.setParameter("dateTo",dateTo);
        List<Object[]> resultList = nativeQuery.getResultList();
        List<Integer> mmsi = new ArrayList<>();

        for (int i=0;i<resultList.size();i++) {
            Object[] shipData = resultList.get(i);
            mmsi.add((Integer) shipData[3]);
        }
        if(!mmsi.isEmpty()) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM Route WHERE ship_mmsi IN(" + mmsi + ")");
            sql.replace(sql.indexOf("["), sql.indexOf("[") + 1, "");
            sql.replace(sql.indexOf("]"), sql.indexOf("]") + 1, "");
            Query nativeQuery2 = entityManager.createNativeQuery(sql.toString());
            routeList = nativeQuery2.getResultList();
        }
        return routeList;
    }


    public List getPopularRoutesByDate(String dateToSearch) {
        String dateFrom = dateToSearch+" 00:00:00";
        String dateTo = dateToSearch+" 23:59:59";
        final Query nativeQuery =entityManager.createNativeQuery("select max(latitude)-min(latitude) as differenceLati,  max(longitude)-min(longitude) as differenceLong ,count(latitude) as meterLati, ship_mmsi from Route WHERE added_time between :dateFrom and :dateTo  group by ship_mmsi having differenceLati>=0.2 or differenceLong>=0.2 limit 20");
        nativeQuery.setParameter("dateFrom",dateFrom);
        nativeQuery.setParameter("dateTo",dateTo);
        List<Object[]> resultList = nativeQuery.getResultList();
        List<Integer> mmsi = new ArrayList<>();

        for (int i=0;i<resultList.size();i++) {
            Object[] user = resultList.get(i);
            mmsi.add((Integer) user[3]);
        }

        return routeRepository.findByShip_MmsiInOrderByShip_Mmsi(mmsi);//must by sorted by mmsi to map view: show route per ship
    }



    public List<Route> getShipRoutesToPort(String dateToSearch,Integer id) {

     return routeRepository.findByDestination_IdOrderByShip_MmsiAscIdAsc(id);

    }
}