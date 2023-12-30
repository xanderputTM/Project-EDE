package fact.it.airportservice.controller;

import fact.it.airportservice.dto.AirportDto;
import fact.it.airportservice.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;


@RestController
@RequestMapping("/api/airport")
@RequiredArgsConstructor
public class AirportController {
    private final AirportService airportService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AirportDto> getAllAirports() {
        return airportService.getAllAirports();
    }

    @GetMapping
    public ResponseEntity<Object> getAirportByCode(@RequestParam String code) {
        return airportService.getAirportByCode(code);
    }
}
