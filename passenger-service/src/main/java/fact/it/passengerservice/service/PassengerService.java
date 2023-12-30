package fact.it.passengerservice.service;

import fact.it.passengerservice.dto.FlightDto;
import fact.it.passengerservice.dto.PassengerDto;
import fact.it.passengerservice.dto.PersonDto;
import fact.it.passengerservice.model.Passenger;
import fact.it.passengerservice.model.Person;
import fact.it.passengerservice.repository.PassengerRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerService {
    private final PassengerRepository passengerRepository;

    private final WebClient webClient;

    @Value("${flightService.baseurl}")
    private String flightServiceBaseUrl;

    @PostConstruct
    public void loadData() {
        if(passengerRepository.count() <= 0){
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
            passenger2.setFlightNumber("2440");
            passenger2.setSeat("1F");
            passenger2.setPnrCode("234KGS");
            passenger2.setPerson(person2);

            Person person3 = new Person();
            person3.setBirthDate(new Date());
            person3.setFirstName("Dirk");
            person3.setLastName("Jenkins");
            person3.setNationality("Belgium");

            Passenger passenger3 = new Passenger();
            passenger3.setHasCheckedIn(true);
            passenger3.setFlightNumber("2280");
            passenger3.setSeat("32A");
            passenger3.setPnrCode("QSGD");
            passenger3.setPerson(person3);

            passengerRepository.save(passenger1);
            passengerRepository.save(passenger2);
            passengerRepository.save(passenger3);
        }
    }

    public List<PassengerDto> getAllPassengerDtosByFlightNumber(String flightNumber) {
        List<Passenger> passengers = passengerRepository.findByFlightNumber(flightNumber);

        return passengers
                .stream()
                .map(this::mapToPassengerDto)
                .toList();
    }

    public List<Passenger> getAllPassengersByFlightNumber(String flightNumber) {
        List<Passenger> passengers = passengerRepository.findByFlightNumber(flightNumber);

        return passengers;
    }

    public ResponseEntity<Object> flightHasSpace(String flightNumber) {
        try {
            ResponseEntity<FlightDto> flightResponse = webClient.get()
                    .uri("http://" + flightServiceBaseUrl + "/api/flight",
                            uriBuilder -> uriBuilder.queryParam("flightNumber", flightNumber).build())
                    .retrieve()
                    .toEntity(FlightDto.class)
                    .block();

            if (flightResponse != null && flightResponse.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
            }

            if (flightResponse == null || flightResponse.getBody() == null) {
                return new ResponseEntity<>("Error!", HttpStatus.BAD_REQUEST);
            }

            Integer capacity = flightResponse.getBody().getCapacity();
            Boolean hasSpace = capacity > getAllPassengersByFlightNumber(flightNumber).size();

            return new ResponseEntity<>(hasSpace, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
        }



    }

    public ResponseEntity<Object> createPassenger(PassengerDto passengerDto) {
        if (passengerRepository.existsByPnrCode(passengerDto.getPnrCode())) {
            return new ResponseEntity<>("There is already a passenger with that pnr code!", HttpStatus.BAD_REQUEST);
        }

        try {
            ResponseEntity<FlightDto> flightResponse = webClient.get()
                    .uri("http://" + flightServiceBaseUrl + "/api/flight",
                            uriBuilder -> uriBuilder.queryParam("flightNumber", passengerDto.getFlightNumber()).build())
                    .retrieve()
                    .toEntity(FlightDto.class)
                    .block();

            if (flightResponse != null && flightResponse.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
        }

        if (passengerRepository.existsByFlightNumberAndSeat(passengerDto.getFlightNumber(), passengerDto.getSeat())) {
            return new ResponseEntity<>("The given seat is already occupied!", HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<Object> flightHasSpaceResponse = flightHasSpace(passengerDto.getFlightNumber());
        if (flightHasSpaceResponse.getStatusCode() != HttpStatus.OK) {
            return flightHasSpaceResponse;
        }

        if (flightHasSpaceResponse.getBody() != null && (Boolean) flightHasSpaceResponse.getBody()) {
            Passenger passenger = mapToPassenger(passengerDto);
            passengerRepository.save(passenger);
            return new ResponseEntity<>(passenger, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no space remaining on the given flight!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> updatePassenger(String pnrCode, PassengerDto passengerDto) {
        Passenger oldPassenger =  passengerRepository.getByPnrCode(pnrCode);
        Passenger newPassenger = mapToPassenger(passengerDto);

        newPassenger.setId(oldPassenger.getId());

        Person newPerson = newPassenger.getPerson();
        newPerson.setId(oldPassenger.getPerson().getId());
        newPassenger.setPerson(newPerson);

        if (!Objects.equals(pnrCode, newPassenger.getPnrCode())) {
            if (passengerRepository.existsByPnrCode(newPassenger.getPnrCode())) {
                return new ResponseEntity<>("There is already a passenger with that pnr code!", HttpStatus.BAD_REQUEST);
            }
        }

        try {
            ResponseEntity<FlightDto> flightResponse = webClient.get()
                    .uri("http://" + flightServiceBaseUrl + "/api/flight",
                            uriBuilder -> uriBuilder.queryParam("flightNumber", passengerDto.getFlightNumber()).build())
                    .retrieve()
                    .toEntity(FlightDto.class)
                    .block();

            if (flightResponse != null && flightResponse.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("There is no flight with that flight number!", HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<Object> flightHasSpaceResponse = flightHasSpace(passengerDto.getFlightNumber());
        if (flightHasSpaceResponse.getStatusCode() != HttpStatus.OK) {
            return flightHasSpaceResponse;
        }

        if (!Objects.equals(newPassenger.getSeat(), oldPassenger.getSeat()) || !Objects.equals(newPassenger.getFlightNumber(), oldPassenger.getFlightNumber())) {
            if (passengerRepository.existsByFlightNumberAndSeat(newPassenger.getFlightNumber(), newPassenger.getSeat())) {
                return new ResponseEntity<>("The given seat is already occupied!", HttpStatus.BAD_REQUEST);
            }
        }


        if (Objects.equals(oldPassenger.getFlightNumber(), newPassenger.getFlightNumber())) {
            passengerRepository.save(newPassenger);
            return new ResponseEntity<>(newPassenger, HttpStatus.OK);
        } else if (flightHasSpaceResponse.getBody() != null && (Boolean) flightHasSpaceResponse.getBody()) {
            passengerRepository.save(newPassenger);
            return new ResponseEntity<>(newPassenger, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no space remaining on the given flight!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> deleteAllPassengersByFlightNumber(String flightNumber) {
        if (!passengerRepository.existsByFlightNumber(flightNumber)) {
            return new ResponseEntity<>("There are no passengers with that flight number!", HttpStatus.OK);
        }

        passengerRepository.deleteAllByFlightNumber(flightNumber);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    public ResponseEntity<Object> deletePassengerByPnrCode(String pnrCode) {
        if (!passengerRepository.existsByPnrCode(pnrCode)) {
            return new ResponseEntity<>("There is no passenger with that pnr code!", HttpStatus.BAD_REQUEST);
        }

        passengerRepository.deleteByPnrCode(pnrCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Object> updateFlightNumberPassengers(String oldFlightNumber, String newFlightNumber) {
        List<Passenger> passengersToChange = getAllPassengersByFlightNumber(oldFlightNumber);

        passengersToChange.forEach(passenger -> {
            passenger.setFlightNumber(newFlightNumber);
        });

        passengerRepository.saveAll(passengersToChange);
        return new ResponseEntity<>("Passengers updated successfully", HttpStatus.OK);
    }

    private PassengerDto mapToPassengerDto(Passenger passenger) {
        return PassengerDto.builder()
                .pnrCode(passenger.getPnrCode())
                .flightNumber(passenger.getFlightNumber())
                .seat(passenger.getSeat())
                .hasCheckedIn(passenger.getHasCheckedIn())
                .person(mapToPersonDto(passenger.getPerson()))
                .build();
    }

    private Passenger mapToPassenger(PassengerDto passengerDto) {
        return Passenger.builder()
                .pnrCode(passengerDto.getPnrCode())
                .flightNumber(passengerDto.getFlightNumber())
                .seat(passengerDto.getSeat())
                .hasCheckedIn(passengerDto.getHasCheckedIn())
                .person(mapToPerson(passengerDto.getPerson()))
                .build();
    }

    private PersonDto mapToPersonDto(Person person) {
        return PersonDto.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .nationality(person.getNationality())
                .birthDate(person.getBirthDate())
                .build();
    }

    private Person mapToPerson(PersonDto personDto) {
        return Person.builder()
                .firstName(personDto.getFirstName())
                .lastName(personDto.getLastName())
                .nationality(personDto.getNationality())
                .birthDate(personDto.getBirthDate())
                .build();
    }


}
