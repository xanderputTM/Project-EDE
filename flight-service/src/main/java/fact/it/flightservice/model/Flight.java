package fact.it.flight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String flightNumber;

    private boolean isDepartingFlight;

    private String remoteAirportCode;

    private DateTime scheduledTime;

    private String gateNumber;

    private String registrationNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Passenger> passengerList;

}