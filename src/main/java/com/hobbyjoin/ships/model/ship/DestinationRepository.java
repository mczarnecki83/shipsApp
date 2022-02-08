package com.hobbyjoin.ships.model.ship;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "destinations", path = "destinations")
public interface DestinationRepository extends CrudRepository<Destination,Integer> {
    Optional<Destination> findByName(String name);
    List<Destination> findByBlackList(Boolean isBlckList);
    List<Destination> findAll();

    List<Destination> findByUserId(long id);

}