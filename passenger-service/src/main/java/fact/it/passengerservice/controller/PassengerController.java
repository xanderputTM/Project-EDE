package fact.it.passengerservice.controller;

import fact.it.passengerservice.dto.PassengerDto;
import fact.it.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @GetMapping("/flight")
    @ResponseStatus(HttpStatus.OK)
    public List<PassengerDto> getAllPassengersByFlightNumber(@RequestParam String flightNumber) {
        return passengerService.getAllPassengersByFlightNumber(flightNumber);
    }

    @PostMapping
    public ResponseEntity<Object> createPassenger(@RequestBody PassengerDto passengerDto) {
        return passengerService.createPassenger(passengerDto);
    }

    @PutMapping
    public ResponseEntity<Object> updatePassenger(@RequestParam String pnrCode, @RequestBody PassengerDto passengerDto) {
        return passengerService.updatePassenger(pnrCode, passengerDto);
    }

    @PutMapping("/flight")
    public ResponseEntity<Object> updateFlightNumberPassengers(@RequestParam String oldFlightNumber, @RequestParam String newFlightNumber) {
        return passengerService.updateFlightNumberPassengers(oldFlightNumber, newFlightNumber);
    }

    @DeleteMapping("/flight")
    public ResponseEntity<Object> deleteAllPassengersByFlightNumber(@RequestParam String flightNumber) {
        return passengerService.deleteAllPassengersByFlightNumber(flightNumber);
    }

    @DeleteMapping
    public ResponseEntity<Object> deletePassengerByPnrCode(@RequestParam String pnrCode) {
        return passengerService.deletePassengerByPnrCode(pnrCode);
    }
}
