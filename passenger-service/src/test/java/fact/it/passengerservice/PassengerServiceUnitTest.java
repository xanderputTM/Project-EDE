package fact.it.passengerservice;

import fact.it.passengerservice.dto.FlightDto;
import fact.it.passengerservice.dto.PassengerDto;
import fact.it.passengerservice.dto.PersonDto;
import fact.it.passengerservice.model.Passenger;
import fact.it.passengerservice.model.Person;
import fact.it.passengerservice.repository.PassengerRepository;
import fact.it.passengerservice.service.PassengerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.springframework.test.util.ReflectionTestUtils.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceUnitTest {

    @InjectMocks
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


//    @Test
//    public void testGetAllPassengerDtosByFlightNumber() throws Exception {
//        // Arrange
//        PassengerService passengerServiceSpy = PowerMockito.spy(new YourPassengerService());
//
//        Person person = new Person();
//        person.setBirthDate(new Date());
//        person.setFirstName("Joe");
//        person.setLastName("Jenkins");
//        person.setNationality("Belgium");
//
//        Passenger passenger = new Passenger();
//        passenger.setHasCheckedIn(true);
//        passenger.setFlightNumber("2280");
//        passenger.setSeat("1E");
//        passenger.setPnrCode("123ABC");
//        passenger.setPerson(person);
//
//        when(passengerRepository.findByFlightNumber("2280")).thenReturn(Arrays.asList(passenger));
//
//        // Mock the behavior of mapToPersonDto
//        PersonDto expectedPersonDto = new PersonDto("Joe", "Jenkins", "Belgium", new Date());
//        PowerMockito.doReturn(expectedPersonDto).when(passengerServiceSpy, "mapToPersonDto", any(Person.class));
//
//        // Act
//        List<PassengerDto> passengerDtos = passengerServiceSpy.getAllPassengerDtosByFlightNumber("2280");
//
//        // Assert
//        assertEquals(1, passengerDtos.size());
//        assertEquals("2280", passengerDtos.get(0).getFlightNumber());
//        assertEquals("1E", passengerDtos.get(0).getSeat());
//        assertEquals("123ABC", passengerDtos.get(0).getPnrCode());
//        assertEquals(expectedPersonDto, passengerDtos.get(0).getPerson());
//
//        verify(passengerRepository, times(1)).findByFlightNumber(passenger.getFlightNumber());
//    }




    //TODO: tests van passenger nog fiksen

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

    }

    @Test
    public void testFlightHasSpace() {
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

    }

//    @Test
//    public void testCreatePassenger() {
//        PersonDto personDto = new PersonDto();
//        personDto.setBirthDate(new Date());
//        personDto.setFirstName("Joe");
//        personDto.setLastName("Jenkins");
//        personDto.setNationality("Belgium");
//
//        PassengerDto passengerDto = new PassengerDto();
//        passengerDto.setPnrCode("123ABC");
//        passengerDto.setSeat("1E");
//        passengerDto.setHasCheckedIn(true);
//        passengerDto.setFlightNumber("2280");
//        passengerDto.setPerson(personDto);
//
//
//        // Act
//        passengerService.createPassenger(passengerDto);
//
//        // Assert
//        verify(passengerRepository, times(1)).save(any(Passenger.class));
//    }

    //    @Test
//    public void testUpdatePassenger() {
//        // Arrange
//        Passenger oldPassenger = new Passenger();
//        oldPassenger.setId("3");
//        oldPassenger.setFlightNumber("2280");
//        oldPassenger.setSeat("1D");
//        oldPassenger.setPnrCode("123ABC");
//        oldPassenger.setPerson(new Person());
//
//        PassengerDto passengerDto = new PassengerDto();
//        passengerDto.setFlightNumber("2280");
//        passengerDto.setSeat("1E");
//        passengerDto.setPnrCode("123ABC");
//        passengerDto.setPerson(new PersonDto());
//
//        Passenger newPassenger = new Passenger();
//        newPassenger.setId(oldPassenger.getId());
//        newPassenger.setFlightNumber(passengerDto.getFlightNumber());
//        newPassenger.setSeat(passengerDto.getSeat());
//        newPassenger.setPnrCode(passengerDto.getPnrCode());
//        newPassenger.setPerson(new Person());
//
//        when(passengerRepository.getByPnrCode("123ABC")).thenReturn(oldPassenger);
//
//        // Act
//        boolean updated = passengerService.updatePassenger("123ABC", passengerDto);
//
//        // Assert
//        assertTrue(updated);
//        assertEquals(newPassenger.getId(), "3");
//        assertEquals(newPassenger.getFlightNumber(), "2280");
//        assertEquals(newPassenger.getSeat(), "1E");
//        assertEquals(newPassenger.getPnrCode(), "123ABC");
//
//        verify(passengerRepository, times(1)).getByPnrCode("123ABC");
//        verify(passengerRepository, times(1)).save(newPassenger);
//    }

}


