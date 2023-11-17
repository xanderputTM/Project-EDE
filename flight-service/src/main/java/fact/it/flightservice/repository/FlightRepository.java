package fact.it.flightservice.repository;

import fact.it.flightservice.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long>{
}