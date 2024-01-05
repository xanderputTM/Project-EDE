package fact.it.passengerservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String pnrCode;
    private String seat;
    private Boolean hasCheckedIn;
    private String flightNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;
}
