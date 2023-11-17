package fact.it.flightservice.service;

import fact.it.flightservice.dto.*;
import fact.it.flightservice.model.Flight;
import fact.it.flightservice.repository.FlightRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

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