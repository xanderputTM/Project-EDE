package fact.it.gateservice.service;

import fact.it.gateservice.dto.*;
import fact.it.gateservice.model.Gate;
import fact.it.gateservice.repository.GateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GateService {

    private final GateRepository gateRepository;

    @PostConstruct
    public void loadData() {
        if(gateRepository.count() <= 0){
            for (int i = 1; i <= 5; i++) {
                Gate gate = new Gate();
                gate.setNumber(String.valueOf(i));
                gate.setAirportCode("M032");
                gateRepository.save(gate);
            }
        }
    }


    public List<GateDto> getAllGates() {
        List<Gate> gates = gateRepository.findAll();

        return gates.stream().map(this::mapToGateResponse).toList();
    }

    public List<GateDto> getGatesByAirportCode(String airportCode) {
        List<Gate> gates = gateRepository.findAllByAirportCode(airportCode);

        return gates.stream().map(this::mapToGateResponse).toList();
    }

    public ResponseEntity<Object> getGateByAirportCodeAndGateNumber(String airportCode, String gateNumber) {
        if (!gateRepository.existsByAirportCodeAndNumber(airportCode, gateNumber)) {
            return new ResponseEntity<>("There is no gate with that airport code and gate number!", HttpStatus.BAD_REQUEST);
        }

        Gate gate = gateRepository.findByAirportCodeAndNumber(airportCode, gateNumber);
        return new ResponseEntity<>(mapToGateResponse(gate), HttpStatus.OK);
    }

    private GateDto mapToGateResponse(Gate gate) {
        return GateDto.builder()
                .number(gate.getNumber())
                .airportCode(gate.getAirportCode())
                .build();
    }
}