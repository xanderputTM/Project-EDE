package fact.it.airportservice.service;

import fact.it.airportservice.dto.AirportDto;
import fact.it.airportservice.model.Airport;
import fact.it.airportservice.repository.AirportRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {
    private final AirportRepository airportRepository;


    @PostConstruct
    public void loadData() {
        if(airportRepository.count() <= 0){
            Airport airport1 = new Airport();
            airport1.setName("Airport 1");
            airport1.setCity("Mol");
            airport1.setCode("M032");
            airport1.setCountry("Belgium");

            Airport airport2 = new Airport();
            airport2.setName("Airport 2");
            airport2.setCity("Geel");
            airport2.setCode("G0331");
            airport2.setCountry("Belgium");

            airportRepository.save(airport1);
            airportRepository.save(airport2);
        }
    }

    public List<AirportDto> getAllAirports() {
        List<Airport> products = airportRepository.findAll();

        return products.stream().map(this::mapToAirportResponse).toList();
    }

    public AirportDto getAirportByCode(String code) {
        return mapToAirportResponse(airportRepository.findByCode(code));
    }

    private AirportDto mapToAirportResponse(Airport airport) {
        return AirportDto.builder()
                .code(airport.getCode())
                .name(airport.getName())
                .country(airport.getCountry())
                .city(airport.getCity())
                .build();
    }


}
