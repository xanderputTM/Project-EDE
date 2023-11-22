package fact.it.flightservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "flights")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String flightNumber;

    private boolean isDepartingFlight;

    private String remoteAirportCode;

    private Date scheduledTime;

    private String gateNumber;

    private String registrationNumber;

}