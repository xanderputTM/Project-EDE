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
            passenger1.setPerson(person2);

            passengerRepository.save(passenger1);
            passengerRepository.save(passenger2);
        }
    }

    public List<PassengerDto> getAllPassengersByFlightNumber(String flightNumber) {
        List<Passenger> passengers = passengerRepository.findByFlightNumber(flightNumber);

        return passengers
                .stream()
                .map(this::mapToPassengerDto)
                .toList();
    }

    private boolean flightHasSpace(String flightNumber) {
        FlightDto flightDto = webClient.get()
                .uri("http://" + flightServiceBaseUrl + "/api/flight",
                        uriBuilder -> uriBuilder.queryParam("flightNumber", flightNumber).build())
                .retrieve()
                .bodyToMono(FlightDto.class)
                .block();

        Integer capacity = flightDto.getCapacity();

        if (getAllPassengersByFlightNumber(flightNumber).size() < capacity) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createPassenger(PassengerDto passengerDto) {
        if (flightHasSpace(passengerDto.getFlightNumber())) {
            Passenger passenger = mapToPassenger(passengerDto);
            passengerRepository.save(passenger);
            return true;
        } else {
            return false;
        }
    }

    public boolean updatePassenger(String pnrCode, PassengerDto passengerDto) {
        Passenger oldPassenger =  passengerRepository.getByPnrCode(pnrCode);
        Passenger newPassenger = mapToPassenger(passengerDto);

        newPassenger.setId(oldPassenger.getId());

        Person newPerson = newPassenger.getPerson();
        newPerson.setId(oldPassenger.getPerson().getId());
        newPassenger.setPerson(newPerson);


        if (Objects.equals(oldPassenger.getFlightNumber(), newPassenger.getFlightNumber())) {
            passengerRepository.save(newPassenger);
            return true;
        } else if (flightHasSpace(newPassenger.getFlightNumber())) {
            passengerRepository.save(newPassenger);
            return true;
        } else {
            return false;
        }

    }

    public void deleteAllPassengersByFlightNumber(String flightNumber) {
        passengerRepository.deleteAllByFlightNumber(flightNumber);
    }

    public void deletePassengerByPnrCode(String pnrCode) {
        passengerRepository.deleteByPnrCode(pnrCode);
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
