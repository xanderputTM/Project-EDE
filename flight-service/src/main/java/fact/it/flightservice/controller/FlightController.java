package fact.it.flightservice.controller;

//import fact.it.flightservice.dto.FlightRequest;
import fact.it.flightservice.dto.FlightResponse;
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

//    @PostMapping
//    @ResponseStatus(HttpStatus.OK)
//    public void createFlight
//            (@RequestBody FlightRequest flightRequest) {
//        flightService.createFlight(flightRequest);
//    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<FlightResponse> getAllFlightsBySkuCode
//            (@RequestParam List<String> skuCode) {
//        return flightService.getAllFlightsBySkuCode(skuCode);
//    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<FlightResponse> getAllFlights() {
        return flightService.getAllFlights();
    }
}

