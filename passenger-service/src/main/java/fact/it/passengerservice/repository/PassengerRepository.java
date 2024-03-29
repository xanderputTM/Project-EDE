package fact.it.passengerservice.repository;

import fact.it.passengerservice.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    List<Passenger> findByFlightNumber(String flightNumber);
    Boolean existsByFlightNumber(String flightNumber);
    Boolean existsByFlightNumberAndSeat(String flightNumber, String seat);
    Boolean existsByPnrCode(String pnrCode);
    Passenger getByPnrCode(String pnrCode);
    void deleteAllByFlightNumber(String flightNumber);
    void deleteByPnrCode(String pnrCode);
}
