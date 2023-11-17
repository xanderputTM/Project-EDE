package fact.it.gateservice.service;

import fact.it.gateservice.dto.*;
import fact.it.gateservice.model.Gate;
import fact.it.gateservice.repository.GateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GateService {

    private final GateRepository gateRepository;


    public List<GateResponse> getAllGates() {
        List<Gate> gates = gateRepository.findAll();

        return gates.stream().map(this::mapToGateResponse).toList();
    }

    private GateResponse mapToGateResponse(Gate gate) {
        return GateResponse.builder()
                .number(gate.getNumber())
                .airportCode(gate.getAirportCode())
                .build();
    }
}