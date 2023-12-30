package fact.it.gateservice.controller;

import fact.it.gateservice.dto.GateDto;
import fact.it.gateservice.service.GateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gate")
@RequiredArgsConstructor
public class GateController {

    private final GateService gateService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<GateDto> getAllGates() { return gateService.getAllGates(); }

    @GetMapping("/airport/all")
    @ResponseStatus(HttpStatus.OK)
    public List<GateDto> getGatesByAirportCode(@RequestParam String airportCode) {
        return gateService.getGatesByAirportCode(airportCode);
    }

    @GetMapping("/airport")
    public ResponseEntity<Object> getGateByAirportCodeAndGateNumber(@RequestParam String airportCode, @RequestParam String gateNumber) {
        return gateService.getGateByAirportCodeAndGateNumber(airportCode, gateNumber);
    }
}