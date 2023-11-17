package fact.it.gateservice.controller;

import fact.it.gateservice.dto.GateResponse;
import fact.it.gateservice.service.GateService;
import fact.it.gateservice.model.Gate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gate")
@RequiredArgsConstructor
public class GateController {

    private final GateService gateService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GateResponse> getAllGates() { return gateService.getAllGates(); }
}