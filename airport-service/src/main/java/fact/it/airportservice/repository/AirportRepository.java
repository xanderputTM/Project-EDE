package fact.it.airportservice.repository;

import fact.it.airportservice.model.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends MongoRepository<Airport, String> {
    Airport findByCode(String code);
    Boolean existsAirportByCode(String code);
}
