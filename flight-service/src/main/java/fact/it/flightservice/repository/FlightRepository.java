package fact.it.flightservice.repository;

import fact.it.flightservice.model.Flight;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer>{
    Flight getFlightByFlightNumber(String flightNumber);
    List<Flight> getFlightsByGateNumber(String gateNumber);
    void deleteByFlightNumber(String flightNumber);
}