package fact.it.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private String flightNumber;
    private boolean isDepartingFlight;
    private String remoteAirportCode;
    private Date scheduledTime;
    private String gateNumber;
    private String registrationNumber;
    private Integer capacity;
}