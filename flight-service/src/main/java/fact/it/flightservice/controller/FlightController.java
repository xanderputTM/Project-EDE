package fact.it.flightservice.controller;

import fact.it.flightservice.dto.FlightDto;
import fact.it.flightservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public FlightDto getFlightByFlightNumber(@RequestParam String flightNumber) {
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
    @ResponseStatus(HttpStatus.OK)
    public void createFlight(@RequestBody FlightDto flightDto) {
        flightService.createFlight(flightDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateFlight(@RequestParam String flightNumber, @RequestBody FlightDto flightDto) {
        flightService.updateFlight(flightNumber, flightDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@RequestParam String flightNumber) {
        flightService.deleteFlight(flightNumber);
    }
}

