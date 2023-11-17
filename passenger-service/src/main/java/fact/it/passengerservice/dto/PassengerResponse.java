package fact.it.passengerservice.dto;

import fact.it.passengerservice.model.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerResponse {
    private String seat;
    private Boolean hasCheckedIn;
    private String flightNumber;
    private PersonResponse person;
}
