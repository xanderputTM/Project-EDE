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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

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

    // TODO: testen voor update en delete schrijven
//    @Test
//    public void testUpdateFlight_Successful() {
//        // Arrange
//        String flightNumber = "123";
//        FlightDto flightDto = new FlightDto();
//        Flight oldFlight = new Flight();
//        oldFlight.setId(1);
//        oldFlight.setFlightNumber(flightNumber);
//        when(flightRepository.existsFlightByFlightNumber(flightNumber)).thenReturn(true);
//        when(flightRepository.getFlightByFlightNumber(flightNumber)).thenReturn(oldFlight);
//        when(webClientBuilder.build()).thenReturn(webClient);
//
//        // Mock the WebClient response for passenger retrieval
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(PassengerDto[].class)).thenReturn(Mono.just(new PassengerDto[]{new PassengerDto()}));
//
//        // Mock the WebClient response for updating passenger flight number
//        when(webClient.put()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
//
//        // Mock the body method separately
//        when(requestBodyUriSpec.body(any())).thenAnswer(invocation -> {
//            Object argument = invocation.getArgument(0);
//            // Perform any necessary processing on the argument, if needed
//            return requestBodyUriSpec;  // Return requestBodyUriSpec directly
//        });
//
//        // Simulate a successful response
//        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(ResponseEntity.ok().build()));
//
//
//
//
//        // Act
//        ResponseEntity<Object> responseEntity = flightService.updateFlight(flightNumber, flightDto);
//
//        // Assert
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//        // Verify
//        verify(flightRepository, times(1)).save(any(Flight.class));
//        verify(webClient, times(1)).put();
//        verify(requestBodyUriSpec, times(1)).body(any());
//        verify(responseSpec, times(1)).toEntity(String.class);
//    }

//    @Test
//    public void testDeleteFlight_Successful() {
//        // Arrange
//        String flightNumber = "123";
//        when(flightRepository.existsFlightByFlightNumber(flightNumber)).thenReturn(true);
//
//        // Mock successful deletion of passengers
//        when(webClient.delete())
//                .thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString()))
//                .thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.retrieve())
//                .thenReturn(responseSpec);
//        when(responseSpec.toEntity(Void.class))
//                .thenReturn(Mono.just(ResponseEntity.ok().build()));
//
//        // Act
//        ResponseEntity<Object> responseEntity = flightService.deleteFlight(flightNumber);
//
//        // Assert
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//        // Verify
//        verify(flightRepository, times(1)).deleteByFlightNumber(flightNumber);
//        verify(webClient, times(1)).delete();
//
//        // Additional verification for the WebClient call
//        verify(webClient, times(1)).delete();
//        verify(requestHeadersUriSpec, times(1)).uri(anyString());
//    }




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

    @Test
    public void testDeleteFlight_ErrorDeletingPassengers() {
        // Arrange
        String flightNumber = "123";
        when(flightRepository.existsFlightByFlightNumber(flightNumber)).thenReturn(true);

        // Mock error deleting passengers
        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(Void.class)).thenReturn(Mono.just(ResponseEntity.badRequest().build()));

        // Act
        ResponseEntity<Object> responseEntity = flightService.deleteFlight(flightNumber);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error deleting passengers.", responseEntity.getBody());

        // Verify
        verify(flightRepository, times(0)).deleteByFlightNumber(any());
        verify(webClient, times(1)).delete();
    }


}
