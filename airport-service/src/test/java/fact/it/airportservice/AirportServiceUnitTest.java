package fact.it.airportservice;

import fact.it.airportservice.dto.AirportDto;
import fact.it.airportservice.model.Airport;
import fact.it.airportservice.repository.AirportRepository;
import fact.it.airportservice.service.AirportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Console;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AirportServiceUnitTest {

    @InjectMocks
    private AirportService airportService;

    @Mock
    private AirportRepository airportRepository;

    @Test
    public void testGetAllAirports() {
        // Arrange
        Airport airport = new Airport();
        airport.setId("1");
        airport.setName("Test airport");
        airport.setCity("Merret");
        airport.setCode("M2450");
        airport.setCountry("Belgium");


        when(airportRepository.findAll()).thenReturn(Arrays.asList(airport));

        // Act
        List<AirportDto> airports = airportService.getAllAirports();

        // Assert
        assertTrue(airports != null);
        assertEquals(1, airports.size());
        assertEquals("Test airport", airports.get(0).getName());
        assertEquals("Merret", airports.get(0).getCity());
        assertEquals("M2450", airports.get(0).getCode());
        assertEquals("Belgium", airports.get(0).getCountry());

        verify(airportRepository, times(1)).findAll();
    }

    @Test
    public void testGetAirportByCode_AirportFound() {
        // Arrange
        Airport airport = new Airport();
        airport.setId("1");
        airport.setName("Test airport");
        airport.setCity("Merret");
        airport.setCode("M2450");
        airport.setCountry("Belgium");

        when(airportRepository.findByCode("M2450")).thenReturn(airport);
        when(airportRepository.existsAirportByCode("M2450")).thenReturn(true);

        // Act
        AirportDto airportDto = (AirportDto) airportService.getAirportByCode("M2450").getBody();

        // Assert
        assertEquals("Test airport", airport.getName());
        assertEquals("Merret", airport.getCity());
        assertEquals("M2450", airport.getCode());
        assertEquals("Belgium", airport.getCountry());

        verify(airportRepository, times(1)).findByCode(airportDto.getCode());
        verify(airportRepository, times(1)).existsAirportByCode(airportDto.getCode());
    }

    @Test
    public void testGetAirportByCode_CodeDoesNotExist() {
        // Arrange
        Airport airport = new Airport();
        airport.setId("1");
        airport.setName("Test airport");
        airport.setCity("Merret");
        airport.setCode("M2450");
        airport.setCountry("Belgium");

        when(airportRepository.existsAirportByCode("TEST")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = airportService.getAirportByCode("TEST");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Object responseBody = responseEntity.getBody();
        assertTrue(responseBody instanceof String);

        String errorMessage = (String) responseBody;
        assertEquals("There is no airport with that code!", errorMessage);

        verify(airportRepository, never()).findByCode(anyString());
        verify(airportRepository, times(1)).existsAirportByCode("TEST");
    }

}
