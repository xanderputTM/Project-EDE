package fact.it.passengerservice;

import fact.it.passengerservice.dto.FlightDto;
import fact.it.passengerservice.dto.PassengerDto;
import fact.it.passengerservice.dto.PersonDto;
import fact.it.passengerservice.model.Passenger;
import fact.it.passengerservice.model.Person;
import fact.it.passengerservice.repository.PassengerRepository;
import fact.it.passengerservice.service.PassengerService;

import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceUnitTest {

    @InjectMocks
    @Spy
    private PassengerService passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        setField(passengerService, "flightServiceBaseUrl", "http://localhost:8082");
    }

    @Test
    public void testGetAllPassengersByFlightNumber() {
        // Arrange
        Person person = new Person();
        person.setBirthDate(new Date());
        person.setFirstName("Joe");
        person.setLastName("Jenkins");
        person.setNationality("Belgium");

        Passenger passenger = new Passenger();
        passenger.setHasCheckedIn(true);
        passenger.setFlightNumber("2280");
        passenger.setSeat("1E");
        passenger.setPnrCode("123ABC");
        passenger.setPerson(person);

        when(passengerRepository.findByFlightNumber("2280")).thenReturn(Arrays.asList(passenger));

        // Act
        List<Passenger> passengers = passengerService.getAllPassengersByFlightNumber("2280");


        // Assert
        assertEquals(1, passengers.size());
        assertEquals("2280", passenger.getFlightNumber());
        assertEquals("1E", passenger.getSeat());
        assertEquals("123ABC", passenger.getPnrCode());
        assertEquals(person, passenger.getPerson());

        verify(passengerRepository, times(1)).findByFlightNumber(passenger.getFlightNumber());
    }

    @Test
    public void testGetAllPassengersDtosByFlightNumber() {
        // Arrange
        Person person = new Person();
        person.setBirthDate(new Date());
        person.setFirstName("Joe");
        person.setLastName("Jenkins");
        person.setNationality("Belgium");

        Passenger passenger = new Passenger();
        passenger.setHasCheckedIn(true);
        passenger.setFlightNumber("2280");
        passenger.setSeat("1E");
        passenger.setPnrCode("123ABC");
        passenger.setPerson(person);

        when(passengerRepository.findByFlightNumber("2280")).thenReturn(Arrays.asList(passenger));

        // Act
        List<PassengerDto> passengers = passengerService.getAllPassengerDtosByFlightNumber("2280");


        // Assert
        assertEquals(1, passengers.size());
        assertEquals("2280", passenger.getFlightNumber());
        assertEquals("1E", passenger.getSeat());
        assertEquals("123ABC", passenger.getPnrCode());
        assertEquals(person, passenger.getPerson());

        verify(passengerRepository, times(1)).findByFlightNumber(passenger.getFlightNumber());
    }

    @Test
    public void testFlightHasSpaceReturnsTrue() {
        // Arrange
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(5);

        Person person1 = new Person();
        person1.setBirthDate(new Date());
        person1.setFirstName("Joe");
        person1.setLastName("Jenkins");
        person1.setNationality("Belgium");

        Passenger passenger1 = new Passenger();
        passenger1.setHasCheckedIn(true);
        passenger1.setFlightNumber("2280");
        passenger1.setSeat("1E");
        passenger1.setPnrCode("123ABC");
        passenger1.setPerson(person1);

        Person person2 = new Person();
        person2.setBirthDate(new Date());
        person2.setFirstName("George");
        person2.setLastName("Baker");
        person2.setNationality("United Kingdom");

        Passenger passenger2 = new Passenger();
        passenger2.setHasCheckedIn(false);
        passenger2.setFlightNumber("2280");
        passenger2.setSeat("1F");
        passenger2.setPnrCode("234KGS");
        passenger2.setPerson(person2);

        when(passengerRepository.findByFlightNumber(flightDto.getFlightNumber())).thenReturn(Arrays.asList(passenger1, passenger2));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(flightDto, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        // Act
        boolean result = (boolean) passengerService.flightHasSpace(flightDto.getFlightNumber()).getBody();

        // Assert
        assertTrue(result);
        verify(passengerRepository, times(1)).findByFlightNumber(anyString());
        verify(webClient, times(1)).get();
    }

    @Test
    public void testFlightHasSpaceReturnsFalse() {
        // Arrange
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(1);

        Person person1 = new Person();
        person1.setBirthDate(new Date());
        person1.setFirstName("Joe");
        person1.setLastName("Jenkins");
        person1.setNationality("Belgium");

        Passenger passenger1 = new Passenger();
        passenger1.setHasCheckedIn(true);
        passenger1.setFlightNumber("2280");
        passenger1.setSeat("1E");
        passenger1.setPnrCode("123ABC");
        passenger1.setPerson(person1);

        Person person2 = new Person();
        person2.setBirthDate(new Date());
        person2.setFirstName("George");
        person2.setLastName("Baker");
        person2.setNationality("United Kingdom");

        Passenger passenger2 = new Passenger();
        passenger2.setHasCheckedIn(false);
        passenger2.setFlightNumber("2280");
        passenger2.setSeat("1F");
        passenger2.setPnrCode("234KGS");
        passenger2.setPerson(person2);

        when(passengerRepository.findByFlightNumber(flightDto.getFlightNumber())).thenReturn(Arrays.asList(passenger1, passenger2));


        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(flightDto, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        // Act
        boolean result = (boolean) passengerService.flightHasSpace(flightDto.getFlightNumber()).getBody();

        // Assert
        assertFalse(result);

        verify(passengerRepository, times(1)).findByFlightNumber(anyString());
        verify(webClient, times(1)).get();

    }

    @Test
    public void testFlightHasSpaceNonExistingFlight() {
        // Arrange
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(1);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(flightDto, HttpStatus.BAD_REQUEST);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        // Act
        ResponseEntity<Object> result = passengerService.flightHasSpace(flightDto.getFlightNumber());

        // Assert
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(result.getBody(), "There is no flight with that flight number!");

        verify(passengerRepository, never()).findByFlightNumber(anyString());
        verify(webClient, times(1)).get();
    }

    @Test
    public void testFlightHasSpaceError() {
        // Arrange
        when(webClient.get()).thenThrow(WebClientRequestException.class);

        // Act
        ResponseEntity<Object> result = passengerService.flightHasSpace("2280");

        // Assert
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(result.getBody(), "There is no flight with that flight number!");

        verify(passengerRepository, never()).findByFlightNumber(anyString());
        verify(webClient, times(1)).get();
    }

    @Test
    public void testCreatePassenger_Success() {
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(1);

        PersonDto personDto = new PersonDto();
        personDto.setBirthDate(new Date());
        personDto.setFirstName("Joe");
        personDto.setLastName("Jenkins");
        personDto.setNationality("Belgium");

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setPnrCode("123ABC");
        passengerDto.setSeat("1E");
        passengerDto.setHasCheckedIn(true);
        passengerDto.setFlightNumber("2280");
        passengerDto.setPerson(personDto);

        when(passengerRepository.existsByPnrCode(passengerDto.getPnrCode())).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(flightDto, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        when(passengerRepository.existsByFlightNumberAndSeat(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> flightHasSpaceResponse = new ResponseEntity<>(true, HttpStatus.OK);
        Mockito.when(passengerService.flightHasSpace(anyString())).thenReturn(flightHasSpaceResponse);

        // Act
        ResponseEntity<Object> passengerResponse = passengerService.createPassenger(passengerDto);
        PassengerDto returnedPassenger = (PassengerDto) passengerResponse.getBody();

        // Assert
        assertEquals(returnedPassenger.getPnrCode(), passengerDto.getPnrCode());
        assertEquals(returnedPassenger.getSeat(), passengerDto.getSeat());
        assertEquals(returnedPassenger.getFlightNumber(), passengerDto.getFlightNumber());
        assertEquals(returnedPassenger.getHasCheckedIn(), passengerDto.getHasCheckedIn());

        assertEquals(returnedPassenger.getPerson().getFirstName(), passengerDto.getPerson().getFirstName());
        assertEquals(returnedPassenger.getPerson().getLastName(), passengerDto.getPerson().getLastName());
        assertEquals(returnedPassenger.getPerson().getNationality(), passengerDto.getPerson().getNationality());
        assertEquals(returnedPassenger.getPerson().getBirthDate(), passengerDto.getPerson().getBirthDate());

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(passengerRepository, times(1)).existsByFlightNumberAndSeat(any(), any());
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    public void testCreatePassenger_PnrCodeAlreadyExists() {
        PersonDto personDto = new PersonDto();
        personDto.setBirthDate(new Date());
        personDto.setFirstName("Joe");
        personDto.setLastName("Jenkins");
        personDto.setNationality("Belgium");

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setPnrCode("123ABC");
        passengerDto.setSeat("1E");
        passengerDto.setHasCheckedIn(true);
        passengerDto.setFlightNumber("2280");
        passengerDto.setPerson(personDto);

        when(passengerRepository.existsByPnrCode(passengerDto.getPnrCode())).thenReturn(true);

        // Act
        ResponseEntity<Object> response = passengerService.createPassenger(passengerDto);

        // Assert
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(), "There is already a passenger with that pnr code!");

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(webClient, never()).get();
        verify(passengerRepository, never()).existsByFlightNumberAndSeat(any(), any());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    public void testCreatePassenger_FlightDoesntExist() {
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(1);

        PersonDto personDto = new PersonDto();
        personDto.setBirthDate(new Date());
        personDto.setFirstName("Joe");
        personDto.setLastName("Jenkins");
        personDto.setNationality("Belgium");

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setPnrCode("123ABC");
        passengerDto.setSeat("1E");
        passengerDto.setHasCheckedIn(true);
        passengerDto.setFlightNumber("2280");
        passengerDto.setPerson(personDto);

        when(passengerRepository.existsByPnrCode(passengerDto.getPnrCode())).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(flightDto, HttpStatus.BAD_REQUEST);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        // Act
        ResponseEntity<Object> returnedResponse = passengerService.createPassenger(passengerDto);

        // Assert
        assertEquals(returnedResponse.getStatusCode(), HttpStatus.BAD_REQUEST);

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(webClient, times(1)).get();
        verify(passengerRepository, never()).existsByFlightNumberAndSeat(any(), any());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    public void testCreatePassenger_ErrorGettingFlight() {
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(1);

        PersonDto personDto = new PersonDto();
        personDto.setBirthDate(new Date());
        personDto.setFirstName("Joe");
        personDto.setLastName("Jenkins");
        personDto.setNationality("Belgium");

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setPnrCode("123ABC");
        passengerDto.setSeat("1E");
        passengerDto.setHasCheckedIn(true);
        passengerDto.setFlightNumber("2280");
        passengerDto.setPerson(personDto);

        when(passengerRepository.existsByPnrCode(passengerDto.getPnrCode())).thenReturn(false);

        when(webClient.get()).thenThrow(WebClientRequestException.class);

        // Act
        ResponseEntity<Object> returnedResponse = passengerService.createPassenger(passengerDto);

        // Assert
        assertEquals(returnedResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(returnedResponse.getBody(), "There is no flight with that flight number!");

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(webClient, times(1)).get();
        verify(passengerRepository, never()).existsByFlightNumberAndSeat(any(), any());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    public void testCreatePassenger_SeatAlreadyOccupied() {
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(1);

        PersonDto personDto = new PersonDto();
        personDto.setBirthDate(new Date());
        personDto.setFirstName("Joe");
        personDto.setLastName("Jenkins");
        personDto.setNationality("Belgium");

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setPnrCode("123ABC");
        passengerDto.setSeat("1E");
        passengerDto.setHasCheckedIn(true);
        passengerDto.setFlightNumber("2280");
        passengerDto.setPerson(personDto);

        when(passengerRepository.existsByPnrCode(passengerDto.getPnrCode())).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(flightDto, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        when(passengerRepository.existsByFlightNumberAndSeat(anyString(), anyString())).thenReturn(true);

        // Act
        ResponseEntity<Object> returnedResponse = passengerService.createPassenger(passengerDto);

        // Assert
        assertEquals(returnedResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(returnedResponse.getBody(), "The given seat is already occupied!");

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(webClient, times(1)).get();
        verify(passengerRepository, times(1)).existsByFlightNumberAndSeat(any(), any());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    public void testCreatePassenger_HasSpaceBadRequest() {
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(1);

        PersonDto personDto = new PersonDto();
        personDto.setBirthDate(new Date());
        personDto.setFirstName("Joe");
        personDto.setLastName("Jenkins");
        personDto.setNationality("Belgium");

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setPnrCode("123ABC");
        passengerDto.setSeat("1E");
        passengerDto.setHasCheckedIn(true);
        passengerDto.setFlightNumber("2280");
        passengerDto.setPerson(personDto);

        when(passengerRepository.existsByPnrCode(passengerDto.getPnrCode())).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(flightDto, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        when(passengerRepository.existsByFlightNumberAndSeat(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> flightHasSpaceResponse = new ResponseEntity<>(true, HttpStatus.BAD_REQUEST);
        Mockito.when(passengerService.flightHasSpace(anyString())).thenReturn(flightHasSpaceResponse);

        // Act
        ResponseEntity<Object> returnedResponse = passengerService.createPassenger(passengerDto);

        // Assert
        assertEquals(returnedResponse.getStatusCode(), HttpStatus.BAD_REQUEST);

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(passengerRepository, times(1)).existsByFlightNumberAndSeat(any(), any());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    public void testCreatePassenger_HasNoSpace() {
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("2280");
        flightDto.setCapacity(1);

        PersonDto personDto = new PersonDto();
        personDto.setBirthDate(new Date());
        personDto.setFirstName("Joe");
        personDto.setLastName("Jenkins");
        personDto.setNationality("Belgium");

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setPnrCode("123ABC");
        passengerDto.setSeat("1E");
        passengerDto.setHasCheckedIn(true);
        passengerDto.setFlightNumber("2280");
        passengerDto.setPerson(personDto);

        when(passengerRepository.existsByPnrCode(passengerDto.getPnrCode())).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(flightDto, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        when(passengerRepository.existsByFlightNumberAndSeat(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> flightHasSpaceResponse = new ResponseEntity<>(false, HttpStatus.OK);
        Mockito.when(passengerService.flightHasSpace(anyString())).thenReturn(flightHasSpaceResponse);


        // Act
        ResponseEntity<Object> returnedResponse = passengerService.createPassenger(passengerDto);

        // Assert
        assertEquals(returnedResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(returnedResponse.getBody(), "There is no space remaining on the given flight!");

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(passengerRepository, times(1)).existsByFlightNumberAndSeat(any(), any());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }


    @Test
    public void testUpdatePassenger_Successful() {
        // Arrange
        Passenger oldPassenger = new Passenger();
        oldPassenger.setId("3");
        oldPassenger.setFlightNumber("2280");
        oldPassenger.setSeat("1D");
        oldPassenger.setPnrCode("123ABC");
        oldPassenger.setPerson(new Person());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFlightNumber("2281");
        passengerDto.setSeat("1E");
        passengerDto.setPnrCode("123ABCD");
        passengerDto.setPerson(new PersonDto());

        Passenger newPassenger = new Passenger();
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
        newPassenger.setSeat(passengerDto.getSeat());
        newPassenger.setPnrCode(passengerDto.getPnrCode());
        newPassenger.setPerson(new Person());

        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber("123ABCD");
        newFlight.setCapacity(1);

        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
        when(passengerRepository.existsByPnrCode("123ABCD")).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(newFlight, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        ResponseEntity<Object> flightHasSpaceResponse = new ResponseEntity<>(true, HttpStatus.OK);
        Mockito.when(passengerService.flightHasSpace(anyString())).thenReturn(flightHasSpaceResponse);

        when(passengerRepository.existsByFlightNumberAndSeat(anyString(), anyString())).thenReturn(false);


        // Act
        ResponseEntity<Object> testResponse = passengerService.updatePassenger("123ABC", passengerDto);
        PassengerDto returnedPassenger = (PassengerDto) testResponse.getBody();

        // Assert
        assertEquals(testResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(returnedPassenger.getFlightNumber(), "2281");
        assertEquals(returnedPassenger.getSeat(), "1E");
        assertEquals(returnedPassenger.getPnrCode(), "123ABCD");

        verify(passengerRepository, times(1)).getByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByFlightNumberAndSeat(anyString(), anyString());
        verify(passengerRepository, times(1)).save(any());
    }

    @Test
    public void testUpdatePassenger_PnrCodeTaken() {
        // Arrange
        Passenger oldPassenger = new Passenger();
        oldPassenger.setId("3");
        oldPassenger.setFlightNumber("2280");
        oldPassenger.setSeat("1D");
        oldPassenger.setPnrCode("123ABC");
        oldPassenger.setPerson(new Person());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFlightNumber("2281");
        passengerDto.setSeat("1E");
        passengerDto.setPnrCode("123ABCD");
        passengerDto.setPerson(new PersonDto());

        Passenger newPassenger = new Passenger();
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
        newPassenger.setSeat(passengerDto.getSeat());
        newPassenger.setPnrCode(passengerDto.getPnrCode());
        newPassenger.setPerson(new Person());

        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber("123ABCD");
        newFlight.setCapacity(1);

        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
        when(passengerRepository.existsByPnrCode("123ABCD")).thenReturn(true);

        // Act
        ResponseEntity<Object> testResponse = passengerService.updatePassenger("123ABC", passengerDto);

        // Assert
        assertEquals(testResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(testResponse.getBody(), "There is already a passenger with that pnr code!");

        verify(passengerRepository, times(1)).getByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByPnrCode(anyString());
        verify(passengerRepository, times(0)).existsByFlightNumberAndSeat(anyString(), anyString());
        verify(passengerRepository, never()).save(any());
    }

    @Test
    public void testUpdatePassenger_FlightDoesntExist() {
        // Arrange
        Passenger oldPassenger = new Passenger();
        oldPassenger.setId("3");
        oldPassenger.setFlightNumber("2280");
        oldPassenger.setSeat("1D");
        oldPassenger.setPnrCode("123ABC");
        oldPassenger.setPerson(new Person());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFlightNumber("2281");
        passengerDto.setSeat("1E");
        passengerDto.setPnrCode("123ABCD");
        passengerDto.setPerson(new PersonDto());

        Passenger newPassenger = new Passenger();
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
        newPassenger.setSeat(passengerDto.getSeat());
        newPassenger.setPnrCode(passengerDto.getPnrCode());
        newPassenger.setPerson(new Person());

        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber("123ABCD");
        newFlight.setCapacity(1);

        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
        when(passengerRepository.existsByPnrCode("123ABCD")).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(newFlight, HttpStatus.BAD_REQUEST);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        // Act
        ResponseEntity<Object> testResponse = passengerService.updatePassenger("123ABC", passengerDto);

        // Assert
        assertEquals(testResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(testResponse.getBody(), "There is no flight with that flight number!");

        verify(passengerRepository, times(1)).getByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByPnrCode(anyString());
        verify(passengerRepository, times(0)).existsByFlightNumberAndSeat(anyString(), anyString());
        verify(passengerRepository, never()).save(any());
    }

    @Test
    public void testUpdatePassenger_ErrorGettingFlight() {
        // Arrange
        Passenger oldPassenger = new Passenger();
        oldPassenger.setId("3");
        oldPassenger.setFlightNumber("2280");
        oldPassenger.setSeat("1D");
        oldPassenger.setPnrCode("123ABC");
        oldPassenger.setPerson(new Person());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFlightNumber("2281");
        passengerDto.setSeat("1E");
        passengerDto.setPnrCode("123ABCD");
        passengerDto.setPerson(new PersonDto());

        Passenger newPassenger = new Passenger();
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
        newPassenger.setSeat(passengerDto.getSeat());
        newPassenger.setPnrCode(passengerDto.getPnrCode());
        newPassenger.setPerson(new Person());

        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber("123ABCD");
        newFlight.setCapacity(1);

        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
        when(passengerRepository.existsByPnrCode("123ABCD")).thenReturn(false);

        when(webClient.get()).thenThrow(WebClientRequestException.class);

        // Act
        ResponseEntity<Object> testResponse = passengerService.updatePassenger("123ABC", passengerDto);

        // Assert
        assertEquals(testResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(testResponse.getBody(), "There is no flight with that flight number!");

        verify(passengerRepository, times(1)).getByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByPnrCode(anyString());
        verify(passengerRepository, times(0)).existsByFlightNumberAndSeat(anyString(), anyString());
        verify(passengerRepository, never()).save(any());
    }

    @Test
    public void testUpdatePassenger_FlightHasSpaceBadRequest() {
        // Arrange
        Passenger oldPassenger = new Passenger();
        oldPassenger.setId("3");
        oldPassenger.setFlightNumber("2280");
        oldPassenger.setSeat("1D");
        oldPassenger.setPnrCode("123ABC");
        oldPassenger.setPerson(new Person());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFlightNumber("2281");
        passengerDto.setSeat("1E");
        passengerDto.setPnrCode("123ABCD");
        passengerDto.setPerson(new PersonDto());

        Passenger newPassenger = new Passenger();
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
        newPassenger.setSeat(passengerDto.getSeat());
        newPassenger.setPnrCode(passengerDto.getPnrCode());
        newPassenger.setPerson(new Person());

        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber("123ABCD");
        newFlight.setCapacity(1);

        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
        when(passengerRepository.existsByPnrCode("123ABCD")).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(newFlight, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        ResponseEntity<Object> flightHasSpaceResponse = new ResponseEntity<>("Error from hasSpace function", HttpStatus.BAD_REQUEST);
        Mockito.when(passengerService.flightHasSpace(anyString())).thenReturn(flightHasSpaceResponse);

        // Act
        ResponseEntity<Object> testResponse = passengerService.updatePassenger("123ABC", passengerDto);

        // Assert
        assertEquals(testResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(testResponse.getBody(), "Error from hasSpace function");

        verify(passengerRepository, times(1)).getByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByPnrCode(anyString());
        verify(passengerRepository, times(0)).existsByFlightNumberAndSeat(anyString(), anyString());
        verify(passengerRepository, never()).save(any());
    }

    @Test
    public void testUpdatePassenger_SeatOccupied() {
        // Arrange
        Passenger oldPassenger = new Passenger();
        oldPassenger.setId("3");
        oldPassenger.setFlightNumber("2280");
        oldPassenger.setSeat("1D");
        oldPassenger.setPnrCode("123ABC");
        oldPassenger.setPerson(new Person());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFlightNumber("2281");
        passengerDto.setSeat("1E");
        passengerDto.setPnrCode("123ABCD");
        passengerDto.setPerson(new PersonDto());

        Passenger newPassenger = new Passenger();
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
        newPassenger.setSeat(passengerDto.getSeat());
        newPassenger.setPnrCode(passengerDto.getPnrCode());
        newPassenger.setPerson(new Person());

        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber("123ABCD");
        newFlight.setCapacity(1);

        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
        when(passengerRepository.existsByPnrCode("123ABCD")).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(newFlight, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        ResponseEntity<Object> flightHasSpaceResponse = new ResponseEntity<>(true, HttpStatus.OK);
        Mockito.when(passengerService.flightHasSpace(anyString())).thenReturn(flightHasSpaceResponse);

        when(passengerRepository.existsByFlightNumberAndSeat(anyString(), anyString())).thenReturn(true);


        // Act
        ResponseEntity<Object> testResponse = passengerService.updatePassenger("123ABC", passengerDto);

        // Assert
        assertEquals(testResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(testResponse.getBody(), "The given seat is already occupied!");

        verify(passengerRepository, times(1)).getByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByFlightNumberAndSeat(anyString(), anyString());
        verify(passengerRepository, never()).save(any());
    }

    @Test
    public void testUpdatePassenger_SuccessConstantFlightNumber() {
        // Arrange
        Passenger oldPassenger = new Passenger();
        oldPassenger.setId("3");
        oldPassenger.setFlightNumber("2280");
        oldPassenger.setSeat("1D");
        oldPassenger.setPnrCode("123ABC");
        oldPassenger.setPerson(new Person());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFlightNumber("2280");
        passengerDto.setSeat("1E");
        passengerDto.setPnrCode("123ABCD");
        passengerDto.setPerson(new PersonDto());

        Passenger newPassenger = new Passenger();
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
        newPassenger.setSeat(passengerDto.getSeat());
        newPassenger.setPnrCode(passengerDto.getPnrCode());
        newPassenger.setPerson(new Person());

        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber("123ABCD");
        newFlight.setCapacity(1);

        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
        when(passengerRepository.existsByPnrCode("123ABCD")).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(newFlight, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        ResponseEntity<Object> flightHasSpaceResponse = new ResponseEntity<>(true, HttpStatus.OK);
        Mockito.when(passengerService.flightHasSpace(anyString())).thenReturn(flightHasSpaceResponse);

        when(passengerRepository.existsByFlightNumberAndSeat(anyString(), anyString())).thenReturn(false);


        // Act
        ResponseEntity<Object> testResponse = passengerService.updatePassenger("123ABC", passengerDto);
        PassengerDto returnedPassenger = (PassengerDto) testResponse.getBody();

        // Assert
        assertEquals(testResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(returnedPassenger.getFlightNumber(), "2280");
        assertEquals(returnedPassenger.getSeat(), "1E");
        assertEquals(returnedPassenger.getPnrCode(), "123ABCD");

        verify(passengerRepository, times(1)).getByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByFlightNumberAndSeat(anyString(), anyString());
        verify(passengerRepository, times(1)).save(any());
    }

    @Test
    public void testUpdatePassenger_NoSpaceRemaining() {
        // Arrange
        Passenger oldPassenger = new Passenger();
        oldPassenger.setId("3");
        oldPassenger.setFlightNumber("2280");
        oldPassenger.setSeat("1D");
        oldPassenger.setPnrCode("123ABC");
        oldPassenger.setPerson(new Person());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFlightNumber("2281");
        passengerDto.setSeat("1E");
        passengerDto.setPnrCode("123ABCD");
        passengerDto.setPerson(new PersonDto());

        Passenger newPassenger = new Passenger();
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
        newPassenger.setSeat(passengerDto.getSeat());
        newPassenger.setPnrCode(passengerDto.getPnrCode());
        newPassenger.setPerson(new Person());

        FlightDto newFlight = new FlightDto();
        newFlight.setFlightNumber("123ABCD");
        newFlight.setCapacity(1);

        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
        when(passengerRepository.existsByPnrCode("123ABCD")).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        ResponseEntity<FlightDto> response = new ResponseEntity<>(newFlight, HttpStatus.OK);
        when(responseSpec.toEntity(FlightDto.class)).thenReturn(Mono.just(response));

        ResponseEntity<Object> flightHasSpaceResponse = new ResponseEntity<>(false, HttpStatus.OK);
        Mockito.when(passengerService.flightHasSpace(anyString())).thenReturn(flightHasSpaceResponse);

        when(passengerRepository.existsByFlightNumberAndSeat(anyString(), anyString())).thenReturn(false);


        // Act
        ResponseEntity<Object> testResponse = passengerService.updatePassenger("123ABC", passengerDto);

        // Assert
        assertEquals(testResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(testResponse.getBody(), "There is no space remaining on the given flight!");

        verify(passengerRepository, times(1)).getByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByPnrCode(anyString());
        verify(passengerRepository, times(1)).existsByFlightNumberAndSeat(anyString(), anyString());
        verify(passengerRepository, times(0)).save(any());
    }

    @Test
    public void testDeleteAllPassengersByFlightNumber_FlightExists() {
        // Arrange
        when(passengerRepository.existsByFlightNumber("1")).thenReturn(true);

        // Act
        ResponseEntity<Object> response = passengerService.deleteAllPassengersByFlightNumber("1");

        // Assert
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        verify(passengerRepository, times(1)).existsByFlightNumber(any());
        verify(passengerRepository, times(1)).deleteAllByFlightNumber(any());
    }

    @Test
    public void testDeleteAllPassengersByFlightNumber_FlightDoesntExist() {
        // Arrange
        when(passengerRepository.existsByFlightNumber("1")).thenReturn(false);

        // Act
        ResponseEntity<Object> response = passengerService.deleteAllPassengersByFlightNumber("1");

        // Assert

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "There are no passengers with that flight number!");

        verify(passengerRepository, times(1)).existsByFlightNumber(any());
        verify(passengerRepository, times(0)).deleteAllByFlightNumber(any());
    }
    @Test
    public void testDeletePassengerByPnrCode_Exists() {
        // Arrange
        when(passengerRepository.existsByPnrCode("1")).thenReturn(true);

        // Act
        ResponseEntity<Object> response = passengerService.deletePassengerByPnrCode("1");

        // Assert

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(passengerRepository, times(1)).deleteByPnrCode(any());
    }

    @Test
    public void testDeletePassengerByPnrCode_DoesntExist() {
        // Arrange
        when(passengerRepository.existsByPnrCode("1")).thenReturn(false);

        // Act
        ResponseEntity<Object> response = passengerService.deletePassengerByPnrCode("1");

        // Assert

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(), "There is no passenger with that pnr code!");

        verify(passengerRepository, times(1)).existsByPnrCode(any());
        verify(passengerRepository, times(0)).deleteByPnrCode(any());
    }

    @Test
    public void testUpdateFlightNumberPassengers() {
        // Arrange
        Passenger passenger1 = new Passenger();
        passenger1.setFlightNumber("2280");

        Passenger passenger2 = new Passenger();
        passenger2.setFlightNumber("2280");

        String oldFlightNumber = "2280";
        String newFlightNumber = "2281";

        when(passengerRepository.findByFlightNumber(oldFlightNumber)).thenReturn(Arrays.asList(passenger1, passenger2));

        // Act
        ResponseEntity<Object> response = passengerService.updateFlightNumberPassengers(oldFlightNumber, newFlightNumber);

        // Assert

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Passengers updated successfully");

        verify(passengerRepository, times(1)).findByFlightNumber(any());
        verify(passengerRepository, times(1)).saveAll(any());
    }
}


