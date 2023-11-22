package fact.it.gateservice.service;

import fact.it.gateservice.dto.*;
import fact.it.gateservice.model.Gate;
import fact.it.gateservice.repository.GateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
            Gate gate1 = new Gate();
            gate1.setNumber("2");
            gate1.setAirportCode("G0331");

            Gate gate2 = new Gate();
            gate2.setNumber("8");
            gate2.setAirportCode("M032");

            gateRepository.save(gate1);
            gateRepository.save(gate2);
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

    public GateDto getGateByAirportCodeAndGateNumber(String airportCode, String gateNumber) {
        Gate gate = gateRepository.findByAirportCodeAndNumber(airportCode, gateNumber);
        return mapToGateResponse(gate);
    }

    private GateDto mapToGateResponse(Gate gate) {
        return GateDto.builder()
                .number(gate.getNumber())
                .airportCode(gate.getAirportCode())
                .build();
    }
}