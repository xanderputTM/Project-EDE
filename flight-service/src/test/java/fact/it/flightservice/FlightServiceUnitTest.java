package fact.it.flightservice;

import fact.it.flightservice.dto.FlightDto;
import fact.it.flightservice.dto.PassengerDto;
import fact.it.flightservice.model.Flight;
import fact.it.flightservice.repository.FlightRepository;
import fact.it.flightservice.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceUnitTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(flightService, "passengerServiceBaseUrl", "http://localhost:8084");
    }
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

    @Test
    public void testCreateFlight_FlightAlreadyExists() {
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

        // Mocking: Flight with the same flight number already exists
        when(flightRepository.existsFlightByFlightNumber("2280")).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = flightService.createFlight(flightDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("There is already a flight with that flight number!", responseEntity.getBody());

        verify(flightRepository, times(0)).save(any(Flight.class));  // Ensure that save is not called
        verify(flightRepository, times(1)).existsFlightByFlightNumber("2280");  // Verify existence check
    }

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
        when(flightRepository.existsFlightByFlightNumber("2280")).thenReturn(true);
        // Act
        FlightDto flightDto = (FlightDto) flightService.getFlightByFlightNumber("2280").getBody();

        // Assert
        assertEquals("2280", flight.getFlightNumber());
        assertEquals("12", flight.getGateNumber());
        assertEquals("G0331", flight.getRemoteAirportCode());
        assertEquals("SX-BHR", flight.getRegistrationNumber());
        assertEquals(2, flight.getCapacity());

        verify(flightRepository, times(1)).getFlightByFlightNumber(flight.getFlightNumber());
    }

    @Test
    public void testGetFlightByFlightNumber_FlightNumberDoesNotExist() {
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

        when(flightRepository.existsFlightByFlightNumber("8888")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = flightService.getFlightByFlightNumber("8888");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Object responseBody = responseEntity.getBody();
        assertTrue(responseBody instanceof String);

        String errorMessage = (String) responseBody;
        assertEquals("There is no flight with that flight number!", errorMessage);

        verify(flightRepository, times(0)).getFlightByFlightNumber("8888");  // Correct flight number in verification
        verify(flightRepository, times(1)).existsFlightByFlightNumber("8888");  // Verify existence check
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
    @Test
    public void testUpdateFlight_Successful() {
        // Arrange
        Flight oldFlight = new Flight();
        oldFlight.setId(1);
        String oldFlightNumber = "123";
        Integer oldCapacity = 5;
        oldFlight.setFlightNumber(oldFlightNumber);
        oldFlight.setCapacity(oldCapacity);

        FlightDto flightDto = new FlightDto();
        String newFlightNumber = "1234";
        Integer newCapacity = 6;
        flightDto.setFlightNumber(newFlightNumber);
        flightDto.setCapacity(newCapacity);

        when(flightRepository.existsFlightByFlightNumber(oldFlightNumber)).thenReturn(true);
        when(flightRepository.existsFlightByFlightNumber(newFlightNumber)).thenReturn(false);
        when(flightRepository.getFlightByFlightNumber(oldFlightNumber)).thenReturn(oldFlight);

        // Mock the WebClient response for passenger retrieval
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PassengerDto[].class)).thenReturn(Mono.just(new PassengerDto[]{new PassengerDto()}));

        // Mock the WebClient response for updating passenger flight number
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<String> response = new ResponseEntity<>("test", HttpStatus.OK);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(response));


        // Act
        ResponseEntity<Object> responseEntity = flightService.updateFlight(oldFlightNumber, flightDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify
        verify(flightRepository, times(1)).save(any(Flight.class));
        verify(webClient, times(1)).put(); // Only invoked if flight number is different
        verify(responseSpec, times(1)).toEntity(String.class); // Only invoked if flight number is different
    }

    @Test
    public void testUpdateFlight_FlightNotFound() {
        // Arrange
        String nonExistingFlightNumber = "999";
        FlightDto flightDto = new FlightDto();

        when(flightRepository.existsFlightByFlightNumber(nonExistingFlightNumber)).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = flightService.updateFlight(nonExistingFlightNumber, flightDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("There is no flight with that flight number!", responseEntity.getBody());
        verify(flightRepository, never()).getFlightByFlightNumber(anyString());
        verify(flightRepository, never()).save(any(Flight.class));
        verify(webClient, never()).get();
        verify(webClient, never()).put();
    }

    @Test
    public void testUpdateFlight_CapacityBelowCurrentPassengers() {
        // Arrange
        Flight oldFlight = new Flight();
        oldFlight.setId(1);
        String oldFlightNumber = "123";
        Integer oldCapacity = 5;
        oldFlight.setFlightNumber(oldFlightNumber);
        oldFlight.setCapacity(oldCapacity);

        FlightDto flightDto = new FlightDto();
        String newFlightNumber = "1234";
        Integer newCapacity = 1;
        flightDto.setFlightNumber(newFlightNumber);
        flightDto.setCapacity(newCapacity);

        when(flightRepository.existsFlightByFlightNumber(oldFlightNumber)).thenReturn(true);
        when(flightRepository.getFlightByFlightNumber(oldFlightNumber)).thenReturn(oldFlight);

        // Mock the WebClient response for passenger retrieval
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);

        // Include 2 passengers in the mock response
        when(responseSpec.bodyToMono(PassengerDto[].class))
                .thenReturn(Mono.just(new PassengerDto[]{new PassengerDto(), new PassengerDto()}));

        // Act
        ResponseEntity<Object> responseEntity = flightService.updateFlight(oldFlightNumber, flightDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Capacity is below current amount of passengers!", responseEntity.getBody());
        verify(flightRepository, never()).save(any(Flight.class));
        verify(webClient, times(1)).get();
        verify(webClient, never()).put();
    }



    @Test
    public void testUpdateFlight_DuplicateFlightNumber() {
        // Arrange
        String firstFlightNumber = "123";
        String secondFlightNumber = "456";

        // Create the first flight
        Flight firstFlight = new Flight();
        firstFlight.setId(1);
        firstFlight.setFlightNumber(firstFlightNumber);
        firstFlight.setCapacity(5);

        // Create the second flight
        Flight secondFlight = new Flight();
        secondFlight.setId(2);
        secondFlight.setFlightNumber(secondFlightNumber);
        secondFlight.setCapacity(7);

        // Create the update dto
        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber(secondFlightNumber);
        newFlight.setCapacity(5);

        when(flightRepository.existsFlightByFlightNumber(firstFlightNumber)).thenReturn(true);
        when(flightRepository.getFlightByFlightNumber(firstFlightNumber)).thenReturn(firstFlight);
        when(flightRepository.existsFlightByFlightNumber(secondFlightNumber)).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = flightService.updateFlight(firstFlightNumber, newFlight);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("There is already a flight with that flight number!", responseEntity.getBody());
        verify(flightRepository, never()).save(any(Flight.class));
        verify(webClient, never()).get();
        verify(webClient, never()).put();
    }



    @Test
    public void testDeleteFlight_SuccessfulDeletion() {
        // Arrange
        String flightNumber = "123";

        // Mock the Flight
        Flight mockFlight = new Flight();
        mockFlight.setId(1);
        mockFlight.setFlightNumber(flightNumber);
        mockFlight.setCapacity(5);

        when(flightRepository.existsFlightByFlightNumber(flightNumber)).thenReturn(true);

        // Mock the WebClient response for passenger deletion
        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
        when(responseSpec.toEntity(Void.class)).thenReturn(Mono.just(response));

        // Act
        ResponseEntity<Object> responseEntity = flightService.deleteFlight(flightNumber);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        // Verify
        verify(webClient, times(1)).delete();
        verify(flightRepository, times(1)).deleteByFlightNumber(flightNumber);
    }


    @Test
    public void testDeleteFlight_FlightNotFound() {
        // Arrange
        String flightNumber = "123";
        when(flightRepository.existsFlightByFlightNumber(flightNumber)).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = flightService.deleteFlight(flightNumber);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("There is no flight with that flight number!", responseEntity.getBody());

        // Verify
        verify(flightRepository, times(0)).deleteByFlightNumber(any());
        verify(webClient, times(0)).delete();
    }

    //TODO: deze test nakijken
//    @Test
//    public void testDeleteFlight_ErrorDeletingPassengers() {
//        // Arrange
//        String flightNumber = "123";
//
//        // Mock the Flight
//        Flight mockFlight = new Flight();
//        mockFlight.setId(1);
//        mockFlight.setFlightNumber(flightNumber);
//        mockFlight.setCapacity(5);
//
//        when(flightRepository.existsFlightByFlightNumber(flightNumber)).thenReturn(true);
//
//        // Mock the WebClient response for passenger deletion
//        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestBodySpec);
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//
//        // Simulate an error response from the WebClient
//        when(responseSpec.toEntity(Void.class)).thenReturn(Mono.error(new RuntimeException("Simulated error")));
//
//        // Act
//        ResponseEntity<Object> responseEntity = flightService.deleteFlight(flightNumber);
//
//        // Assert
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertEquals("Error deleting passengers.", responseEntity.getBody());
//
//        // Verify
//        verify(webClient, times(1)).delete();
//        verify(flightRepository, never()).deleteByFlightNumber(any());
//    }

}
