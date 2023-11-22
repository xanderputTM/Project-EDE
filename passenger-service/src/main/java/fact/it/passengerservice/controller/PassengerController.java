package fact.it.passengerservice.controller;

import fact.it.passengerservice.dto.PassengerDto;
import fact.it.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public void createPassenger(@RequestBody PassengerDto passengerDto) {
        passengerService.createPassenger(passengerDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePassenger(@RequestParam String pnrCode, @RequestBody PassengerDto passengerDto) {
        passengerService.updatePassenger(pnrCode, passengerDto);
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
