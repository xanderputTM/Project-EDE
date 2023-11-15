package fact.it.passengerservice.controller;

import fact.it.passengerservice.dto.PassengerResponse;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PassengerResponse> getAllPassengersByFlightCode(@RequestParam String flightNumber) {
        return passengerService.getAllPassengersByFlightNumber(flightNumber);
    }
}
