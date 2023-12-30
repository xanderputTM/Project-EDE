package fact.it.flightservice;

import fact.it.flightservice.dto.FlightDto;
import fact.it.flightservice.model.Flight;
import fact.it.flightservice.repository.FlightRepository;
import fact.it.flightservice.service.FlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceUnitTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @Test
    public void testCreateFlight() {
        // Arrange
        Date currentDate = new Date();

        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setDepartingFlight(true);
        flightDto.setRemoteAirportCode("G0331");
        flightDto.setScheduledTime(currentDate);
        flightDto.setGateNumber("12");
        flightDto.setRegistrationNumber("SX-BHR");
        flightDto.setCapacity(2);

        // Act
        flightService.createFlight(flightDto);

        // Assert
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    //TODO: testUpdate laten werken
//    @Test
//    public void testUpdateFlight() {
//        // Arrange
//        FlightDto updatedFlightDto = new FlightDto();
//        updatedFlightDto.setFlightNumber("2440");
//
//        Flight existingFlight = new Flight();
//        existingFlight.setId(1);
//        existingFlight.setFlightNumber("2280");
//        existingFlight.setDepartingFlight(true);
//        existingFlight.setGateNumber("12");
//        existingFlight.setRemoteAirportCode("G0331");
//        existingFlight.setScheduledTime(new Date());
//        existingFlight.setRegistrationNumber("SX-BHR");
//        existingFlight.setCapacity(2);
//
//        when(flightRepository.getFlightByFlightNumber("2280")).thenReturn(existingFlight);
//
//        // Act
//        flightService.updateFlight("2280", updatedFlightDto);
//
//        // Assert
//        assertEquals("2440", existingFlight.getFlightNumber());
//        assertEquals("12", existingFlight.getGateNumber());
//        assertEquals("G0331", existingFlight.getRemoteAirportCode());
//        assertEquals("SX-BHR", existingFlight.getRegistrationNumber());
//        assertEquals(2, existingFlight.getCapacity());
//
//        verify(flightRepository, times(1)).getFlightByFlightNumber("2280");
//        verify(flightRepository, times(1)).save(existingFlight);
//    }

    @Test
    public void testGetAllFlights() {
        // Arrange
        Flight flight = new Flight();
        flight.setId(1);
        flight.setFlightNumber("2280");
        flight.setDepartingFlight(true);
        flight.setGateNumber("12");
        flight.setRemoteAirportCode("G0331");
        flight.setScheduledTime(new Date());
        flight.setRegistrationNumber("SX-BHR");
        flight.setCapacity(2);

        when(flightRepository.findAll()).thenReturn(Arrays.asList(flight));

        // Act
        List<FlightDto> flights = flightService.getAllFlights();

        // Assert
        assertEquals(1, flights.size());
        assertEquals("2280", flights.get(0).getFlightNumber());
        assertEquals("12", flights.get(0).getGateNumber());
        assertEquals("G0331", flights.get(0).getRemoteAirportCode());
        assertEquals("SX-BHR", flights.get(0).getRegistrationNumber());
        assertEquals(2, flights.get(0).getCapacity());

        verify(flightRepository, times(1)).findAll();
    }
    @Test
    public void testGetFlightByFlightNumber() {
        // Arrange
        Flight flight = new Flight();
        flight.setId(1);
        flight.setFlightNumber("2280");
        flight.setDepartingFlight(true);
        flight.setGateNumber("12");
        flight.setRemoteAirportCode("G0331");
        flight.setScheduledTime(new Date());
        flight.setRegistrationNumber("SX-BHR");
        flight.setCapacity(2);

        when(flightRepository.getFlightByFlightNumber("2280")).thenReturn(flight);

        // Act
        FlightDto flightDto = flightService.getFlightByFlightNumber("2280");

        // Assert
        assertEquals("2280", flight.getFlightNumber());
        assertEquals("12", flight.getGateNumber());
        assertEquals("G0331", flight.getRemoteAirportCode());
        assertEquals("SX-BHR", flight.getRegistrationNumber());
        assertEquals(2, flight.getCapacity());

        verify(flightRepository, times(1)).getFlightByFlightNumber(flight.getFlightNumber());
    }

    @Test
    public void testGetFlightsByGateNumber() {
        // Arrange
        Flight flight = new Flight();
        flight.setId(1);
        flight.setFlightNumber("2280");
        flight.setDepartingFlight(true);
        flight.setGateNumber("12");
        flight.setRemoteAirportCode("G0331");
        flight.setScheduledTime(new Date());
        flight.setRegistrationNumber("SX-BHR");
        flight.setCapacity(2);

        when(flightRepository.getFlightsByGateNumber("12")).thenReturn(Arrays.asList(flight));

        // Act
        List<FlightDto> flights = flightService.getFlightsByGateNumber("12");

        // Assert
        assertEquals(1, flights.size());
        assertEquals("2280", flights.get(0).getFlightNumber());
        assertEquals("12", flights.get(0).getGateNumber());
        assertEquals("G0331", flights.get(0).getRemoteAirportCode());
        assertEquals("SX-BHR", flights.get(0).getRegistrationNumber());
        assertEquals(2, flights.get(0).getCapacity());

        verify(flightRepository, times(1)).getFlightsByGateNumber(flight.getGateNumber());
    }


}
