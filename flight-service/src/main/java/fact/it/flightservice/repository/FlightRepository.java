package fact.it.flightrepository.repository;

import fact.it.flightservice.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Gate, Long>{
}