package fact.it.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerDto {
    private String pnrCode;
    private String seat;
    private Boolean hasCheckedIn;
    private String flightNumber;
    private PersonDto person;
}
