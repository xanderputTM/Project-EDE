package fact.it.passengerservice.service;

import fact.it.passengerservice.dto.PassengerResponse;
import fact.it.passengerservice.dto.PersonResponse;
import fact.it.passengerservice.model.Passenger;
import fact.it.passengerservice.model.Person;
import fact.it.passengerservice.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public List<PassengerResponse> getAllPassengersByFlightNumber(String flightNumber) {
        List<Passenger> passengers = passengerRepository.findByFlightNumber(flightNumber);

        return passengers
                .stream()
                .map(this::mapToPassengerResponse)
                .toList();
    }

    private PassengerResponse mapToPassengerResponse(Passenger passenger) {
        return PassengerResponse.builder()
                .flightNumber(passenger.getFlightNumber())
                .seat(passenger.getSeat())
                .hasCheckedIn(passenger.getHasCheckedIn())
                .person(mapToPersonResponse(passenger.getPerson()))
                .build();
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return PersonResponse.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .nationality(person.getNationality())
                .birthDate(person.getBirthDate())
                .build();
    }
}
