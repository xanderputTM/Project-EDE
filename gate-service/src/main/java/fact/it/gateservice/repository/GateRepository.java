package fact.it.gateservice.repository;

import fact.it.gateservice.model.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GateRepository extends JpaRepository<Gate, Integer>{
    List<Gate> findAllByAirportCode(String airportCode);

    Boolean existsByAirportCodeAndNumber(String airportCode, String gateNumber);

    Gate findByAirportCodeAndNumber(String airportCode, String gateNumber);

}