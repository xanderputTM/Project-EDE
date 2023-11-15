package fact.it.airportservice.service;

import fact.it.airportservice.dto.AirportResponse;
import fact.it.airportservice.model.Airport;
import fact.it.airportservice.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {
    private final AirportRepository airportRepository;

    public List<AirportResponse> getAllAirports() {
        List<Airport> products = airportRepository.findAll();

        return products.stream().map(this::mapToAirportResponse).toList();
    }

    private AirportResponse mapToAirportResponse(Airport airport) {
        return AirportResponse.builder()
                .code(airport.getCode())
                .name(airport.getName())
                .country(airport.getCountry())
                .city(airport.getCity())
                .build();
    }
}
