package com.hobbyjoin.ships.model.ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShipRepository extends PagingAndSortingRepository<Ship,Integer> {
    public Page<Ship> findAll(Pageable pageable);
    public List<Ship> findAllByOrderByNameAsc();
    List<Ship> findByNameOrderByName(String name);
    List<Ship> findByName(String name);
    List<Ship> findByMmsi(int mmsi);
}