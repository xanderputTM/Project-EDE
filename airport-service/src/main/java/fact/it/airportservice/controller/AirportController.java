package fact.it.airportservice.controller;

import fact.it.airportservice.dto.AirportResponse;
import fact.it.airportservice.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/airport")
@RequiredArgsConstructor
public class AirportController {
    private final AirportService airportService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AirportResponse> getAllAirports() {
        return airportService.getAllAirports();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AirportResponse getAirportByCode(@RequestParam String code) {
        return airportService.getAirportByCode(code);
    }
}
