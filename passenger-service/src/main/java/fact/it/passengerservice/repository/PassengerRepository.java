package fact.it.passengerservice.repository;

import fact.it.passengerservice.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    List<Passenger> findByFlightNumber(String flightNumber);
}
