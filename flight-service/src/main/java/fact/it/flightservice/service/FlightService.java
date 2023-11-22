package fact.it.flightservice.service;

import fact.it.flightservice.dto.*;
import fact.it.flightservice.model.Flight;
import fact.it.flightservice.repository.FlightRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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


    public List<FlightDto> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();

        return flights.stream().map(this::mapToFlightDto).toList();
    }

    public FlightDto getFlightByFlightNumber(String flightNumber) {
        Flight flight = flightRepository.getFlightByFlightNumber(flightNumber);
        return mapToFlightDto(flight);
    }

    public List<FlightDto> getFlightsByGateNumber(String gateNumber) {
        List<Flight> flights = flightRepository.getFlightsByGateNumber(gateNumber);

        return flights.stream().map(this::mapToFlightDto).toList();
    }

    public void createFlight(FlightDto flightDto) {
        Flight flight = mapToFlight(flightDto);
        flightRepository.save(flight);
    }

    public void updateFlight(String flightNumber, FlightDto flightDto) {
        Flight oldFlight =  flightRepository.getFlightByFlightNumber(flightNumber);
        Flight newFlight = mapToFlight(flightDto);
        newFlight.setId(oldFlight.getId());

        flightRepository.save(newFlight);
    }

    // TODO : Delete passengers
    public void deleteFlight(String flightNumber) {
        flightRepository.deleteByFlightNumber(flightNumber);
    }

    private FlightDto mapToFlightDto(Flight flight) {
        return FlightDto.builder()
                .flightNumber(flight.getFlightNumber())
                .isDepartingFlight(flight.isDepartingFlight())
                .remoteAirportCode(flight.getRemoteAirportCode())
                .scheduledTime(flight.getScheduledTime())
                .gateNumber(flight.getGateNumber())
                .registrationNumber(flight.getRegistrationNumber())
                .build();
    }

    private Flight mapToFlight(FlightDto flightDto) {
        return Flight.builder()
                .flightNumber(flightDto.getFlightNumber())
                .isDepartingFlight(flightDto.isDepartingFlight())
                .remoteAirportCode(flightDto.getRemoteAirportCode())
                .scheduledTime(flightDto.getScheduledTime())
                .gateNumber(flightDto.getGateNumber())
                .registrationNumber(flightDto.getRegistrationNumber())
                .build();
    }

}