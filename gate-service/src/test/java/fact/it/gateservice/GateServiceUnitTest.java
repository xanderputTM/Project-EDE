package fact.it.gateservice;

import fact.it.gateservice.dto.GateDto;
import fact.it.gateservice.model.Gate;
import fact.it.gateservice.repository.GateRepository;
import fact.it.gateservice.service.GateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GateServiceUnitTest {

    @InjectMocks
    private GateService gateService;

    @Mock
    private GateRepository gateRepository;

    @Test
    public void testGetAllGates() {
        // Arrange
        Gate gate = new Gate();
        gate.setId(1);
        gate.setNumber("2");
        gate.setAirportCode("G0331");

        when(gateRepository.findAll()).thenReturn(Arrays.asList(gate));

        // Act
        List<GateDto> gates = gateService.getAllGates();

        // Assert
        assertEquals(1, gates.size());
        assertEquals("2", gates.get(0).getNumber());
        assertEquals("G0331", gates.get(0).getAirportCode());

        verify(gateRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllGatesByAirportCode() {
        // Arrange
        Gate gate = new Gate();
        gate.setId(1);
        gate.setNumber("2");
        gate.setAirportCode("G0331");

        when(gateRepository.findAllByAirportCode("G0331")).thenReturn(Arrays.asList(gate));

        // Act
        List<GateDto> gates = gateService.getGatesByAirportCode("G0331");

        // Assert
        assertEquals(1, gates.size());
        assertEquals("G0331", gate.getAirportCode());
        assertEquals("2", gate.getNumber());;

        verify(gateRepository, times(1)).findAllByAirportCode(gate.getAirportCode());
    }

    @Test
    public void testGetGateByAirportCodeAndGateNumber() {
        // Arrange
        Gate gate = new Gate();
        gate.setId(1);
        gate.setNumber("2");
        gate.setAirportCode("G0331");

        when(gateRepository.findByAirportCodeAndNumber("G0331", "2")).thenReturn(gate);
        when(gateRepository.existsByAirportCodeAndNumber("G0331", "2")).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = gateService.getGateByAirportCodeAndGateNumber("G0331", "2");

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        GateDto gateDto = (GateDto) responseEntity.getBody();
        assertNotNull(gateDto);
        assertEquals("G0331", gateDto.getAirportCode());
        assertEquals("2", gateDto.getNumber());

        verify(gateRepository, times(1)).findByAirportCodeAndNumber("G0331", "2");
        verify(gateRepository, times(1)).existsByAirportCodeAndNumber("G0331", "2");
    }

    @Test
    public void testGetGateByAirportCodeAndGateNumber_NoGateFound() {
        // Arrange
        when(gateRepository.existsByAirportCodeAndNumber("NonExistentCode", "NonExistentNumber")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = gateService.getGateByAirportCodeAndGateNumber("NonExistentCode", "NonExistentNumber");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("There is no gate with that airport code and gate number!", responseEntity.getBody());

        verify(gateRepository, times(1)).existsByAirportCodeAndNumber("NonExistentCode", "NonExistentNumber");
        verify(gateRepository, never()).findByAirportCodeAndNumber(anyString(), anyString());
    }


}
