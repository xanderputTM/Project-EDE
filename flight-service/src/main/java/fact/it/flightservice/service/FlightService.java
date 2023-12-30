package fact.it.flightservice.service;

import fact.it.flightservice.dto.*;
import fact.it.flightservice.model.Flight;
import fact.it.flightservice.repository.FlightRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightService {

    private final FlightRepository flightRepository;

    private final WebClient webClient;

    @Value("${passengerService.baseurl}")
    private String passengerServiceBaseUrl;

    @PostConstruct
    public void loadData() {
        if(flightRepository.count() <= 0){
            Flight flight1 = new Flight();
            flight1.setFlightNumber("2280");
            flight1.setDepartingFlight(true);
            flight1.setGateNumber("1");
            flight1.setRemoteAirportCode("G0331");
            flight1.setScheduledTime(new Date());
            flight1.setRegistrationNumber("SX-BHR");
            flight1.setCapacity(2);
            flightRepository.save(flight1);


            Flight flight2 = new Flight();
            flight2.setFlightNumber("2440");
            flight2.setDepartingFlight(false);
            flight2.setGateNumber("5");
            flight2.setRemoteAirportCode("G0331");
            flight2.setScheduledTime(new Date());
            flight2.setRegistrationNumber("MV-TTT");
            flight2.setCapacity(3);
            flightRepository.save(flight2);
        }
    }


    public List<FlightDto> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();

        return flights.stream().map(this::mapToFlightDto).toList();
    }

    public ResponseEntity<Object> getFlightByFlightNumber(String flightNumber) {
        if (!flightRepository.existsFlightByFlightNumber(flightNumber)) {
            return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
        }

        Flight flight = flightRepository.getFlightByFlightNumber(flightNumber);
        return new ResponseEntity<>(mapToFlightDto(flight), HttpStatus.OK);
    }

    public List<FlightDto> getFlightsByGateNumber(String gateNumber) {
        List<Flight> flights = flightRepository.getFlightsByGateNumber(gateNumber);

        return flights.stream().map(this::mapToFlightDto).toList();
    }

    public ResponseEntity<Object> createFlight(FlightDto flightDto) {
        Flight flight = mapToFlight(flightDto);

        if (flightRepository.existsFlightByFlightNumber(flight.getFlightNumber())) {
            return new ResponseEntity<>("There is already a flight with that flight number!", HttpStatus.BAD_REQUEST);
        }

        flightRepository.save(flight);
        return new ResponseEntity<>(mapToFlightDto(flight), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updateFlight(String flightNumber, FlightDto flightDto) {
        if (!flightRepository.existsFlightByFlightNumber(flightNumber)) {
            return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
        }

        Flight oldFlight =  flightRepository.getFlightByFlightNumber(flightNumber);
        Flight newFlight = mapToFlight(flightDto);
        newFlight.setId(oldFlight.getId());

        if (!Objects.equals(oldFlight.getCapacity(), newFlight.getCapacity())) {
            PassengerDto[] passengers = webClient.get()
                    .uri("http://" + passengerServiceBaseUrl + "/api/passenger/flight",
                            uriBuilder -> uriBuilder
                                    .queryParam("flightNumber", flightNumber )
                                    .build())
                    .retrieve()
                    .bodyToMono(PassengerDto[].class)
                    .block();

            if (passengers == null || passengers.length > flightDto.getCapacity()) {
                return new ResponseEntity<>("Capacity is below current amount of passengers!", HttpStatus.BAD_REQUEST);
            }
        }

        if (!Objects.equals(flightNumber, newFlight.getFlightNumber())) {
            if (flightRepository.existsFlightByFlightNumber(flightDto.getFlightNumber())) {
                return new ResponseEntity<>("There is already a flight with that flight number!", HttpStatus.BAD_REQUEST);
            }

            ResponseEntity<Object> result = webClient.put()
                .uri("http://" + passengerServiceBaseUrl + "/api/passenger/flight",
                        uriBuilder -> uriBuilder
                                .queryParam("oldFlightNumber", flightNumber )
                                .queryParam("newFlightNumber", flightDto.getFlightNumber() )
                                .build())
                .retrieve().toEntity(Object.class)
                .block();

            if (result != null && result.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("Error updating flight number of existing passengers. Flight has not been updated.", HttpStatus.BAD_REQUEST);
            }
        }

        flightRepository.save(newFlight);
        return new ResponseEntity<>(mapToFlightDto(newFlight), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteFlight(String flightNumber) {
        if (!flightRepository.existsFlightByFlightNumber(flightNumber)) {
            return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<Void> response = webClient.delete()
            .uri("http://" + passengerServiceBaseUrl + "/api/passenger/flight",
                    uriBuilder -> uriBuilder.queryParam("flightNumber", flightNumber).build())
            .retrieve()
            .toEntity(Void.class)
            .block();

        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>("Error deleting passengers.", HttpStatus.BAD_REQUEST);
        }

        flightRepository.deleteByFlightNumber(flightNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private FlightDto mapToFlightDto(Flight flight) {
        return FlightDto.builder()
                .flightNumber(flight.getFlightNumber())
                .isDepartingFlight(flight.isDepartingFlight())
                .remoteAirportCode(flight.getRemoteAirportCode())
                .scheduledTime(flight.getScheduledTime())
                .gateNumber(flight.getGateNumber())
                .registrationNumber(flight.getRegistrationNumber())
                .capacity(flight.getCapacity())
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
                .capacity(flightDto.getCapacity())
                .build();
    }

}