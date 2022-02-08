package com.hobbyjoin.ships.model.ship;

import java.util.List;

public interface ShipDAO {
    List<Ship> get();
    Ship get(int id);
    void save(Ship ship);
}
