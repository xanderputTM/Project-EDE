package fact.it.gateservice.service;

import fact.it.gateservice.dto.*;
import fact.it.gateservice.model.Gate;
import fact.it.gateservice.repository.GateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GateService {

    private final GateRepository gateRepository;
    private final WebClient webClient;

    @Value("${gateService.baseurl}")
    private String gateServiceBaseUrl;

    public List<GateResponse> getAllGates() {
        List<Gate> gates = gateRepository.findAll();

        return gates.stream().map(this::mapToGateResponse).ToList();
    }

    private GateResponse mapToGateResponse(Gate gate) {
        return GateResponse.builder()
                .number(gate.getNumber())
                .airportCode(gate.getAirportCode)
                .build();
    }
}