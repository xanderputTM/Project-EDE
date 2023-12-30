package fact.it.flightservice.controller;

import fact.it.flightservice.dto.FlightDto;
import fact.it.flightservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public ResponseEntity<Object> getFlightByFlightNumber(@RequestParam String flightNumber) {
        return flightService.getFlightByFlightNumber(flightNumber);
    }

    @GetMapping("/gate")
    @ResponseStatus(HttpStatus.OK)
    public List<FlightDto> getFlightsByGateNumber(@RequestParam String gateNumber) {
        return flightService.getFlightsByGateNumber(gateNumber);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<FlightDto> getAllFlights() {
        return flightService.getAllFlights();
    }

    @PostMapping
    public ResponseEntity<Object> createFlight(@RequestBody FlightDto flightDto) {
        return flightService.createFlight(flightDto);
    }

    @PutMapping
    public ResponseEntity<Object> updateFlight(@RequestParam String flightNumber, @RequestBody FlightDto flightDto) {
        return flightService.updateFlight(flightNumber, flightDto);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteFlight(@RequestParam String flightNumber) {
        return flightService.deleteFlight(flightNumber);
    }
}

