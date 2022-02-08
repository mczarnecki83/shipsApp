package com.hobbyjoin.ships.model.ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface RouteRepository extends JpaRepository<Route,Integer> {
    List<Route> findByDestination(String name);
    List<Route> findByShip_Mmsi(int mmsi);
    List<Route> findByShip_MmsiOrderByAddedTime(int mmsi);
    List<Route> findByShip_MmsiIn(List<Integer> mmsi);

    List<Route> findByShip_MmsiInOrderByShip_Mmsi(List<Integer> mmsi);
    //List<Route> findByDestination_IdOrderByShip_Mmsi(int id);
    List<Route> findByDestination_IdOrderByShip_MmsiAscIdAsc(int id);

    //OrderByProgDateAscStartTimeAsc
}
