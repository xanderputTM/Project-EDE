package fact.it.passengerservice.service;

import fact.it.passengerservice.dto.PassengerDto;
import fact.it.passengerservice.dto.PersonDto;
import fact.it.passengerservice.model.Passenger;
import fact.it.passengerservice.model.Person;
import fact.it.passengerservice.repository.PassengerRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerService {
    private final PassengerRepository passengerRepository;

    @PostConstruct
    public void loadData() {
        if(passengerRepository.count() <= 0){
            Passenger passenger1 = new Passenger();
            passenger1.setHasCheckedIn(true);
            passenger1.setFlightNumber("2280");

            Passenger passenger2 = new Passenger();
            passenger2.setHasCheckedIn(false);
            passenger2.setFlightNumber("2440");

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

    public void createPassenger(PassengerDto passengerDto) {
        Passenger passenger = mapToPassenger(passengerDto);
        passengerRepository.save(passenger);
    }

    public void updatePassenger(String pnrCode, PassengerDto passengerDto) {
        Passenger oldPassenger =  passengerRepository.getByPnrCode(pnrCode);
        Passenger newPassenger = mapToPassenger(passengerDto);
        newPassenger.setId(oldPassenger.getId());
        newPassenger.setPerson(oldPassenger.getPerson());

        passengerRepository.save(newPassenger);
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
