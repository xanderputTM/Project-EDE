package fact.it.flightservice.service;

import fact.it.flightservice.dto.*;
import fact.it.flightservice.model.Flight;
import fact.it.flightservice.repository.FlightRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    @PostConstruct
    public void loadData() {
        if(flightRepository.count() <= 0){
            Flight flight1 = new Flight();
            flight1.setFlightNumber("2280");
            flight1.setDepartingFlight(true);
            flight1.setGateNumber("12");
            flight1.setRemoteAirportCode("G0331");
            flight1.setScheduledTime(new Date());
            flight1.setRegistrationNumber("SX-BHR");

            Flight flight2 = new Flight();
            flight2.setFlightNumber("2440");
            flight2.setDepartingFlight(false);
            flight2.setGateNumber("5");
            flight2.setRemoteAirportCode("M032");
            flight2.setScheduledTime(new Date());
            flight2.setRegistrationNumber("MV-TTT");

            flightRepository.save(flight1);
            flightRepository.save(flight2);
        }
    }

    public List<FlightResponse> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();

        return flights.stream().map(this::mapToFlightResponse).toList();
    }

    private FlightResponse mapToFlightResponse(Flight flight) {
        return FlightResponse.builder()
                .flightNumber(flight.getFlightNumber())
                .isDepartingFlight(flight.isDepartingFlight())
                .remoteAirportCode(flight.getRemoteAirportCode())
                .scheduledTime(flight.getScheduledTime())
                .gateNumber(flight.getGateNumber())
                .registrationNumber(flight.getRegistrationNumber())
                .build();
    }
}