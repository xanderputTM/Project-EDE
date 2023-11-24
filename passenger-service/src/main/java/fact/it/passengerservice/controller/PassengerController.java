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
    public ResponseEntity<String> createPassenger(@RequestBody PassengerDto passengerDto) {
        boolean result = passengerService.createPassenger(passengerDto);

        if (result) {
            return new ResponseEntity<>("Passenger created successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Passenger creation failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<String> updatePassenger(@RequestParam String pnrCode, @RequestBody PassengerDto passengerDto) {
        boolean result = passengerService.updatePassenger(pnrCode, passengerDto);

        if (result) {
            return new ResponseEntity<>("Passenger updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Passenger updating failed", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/flight")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllPassengersByFlightNumber(@RequestParam String flightNumber) {
        passengerService.deleteAllPassengersByFlightNumber(flightNumber);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deletePassengerByPnrCode(@RequestParam String pnrCode) {
        passengerService.deletePassengerByPnrCode(pnrCode);
    }
}
