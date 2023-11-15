package fact.it.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {
    private String flightNumber;
    private boolean isDepartingFlight;
    private String RemoteAirportCode;
    private DateTime scheduledTime;
    private String gateNumber;
    private String RegistrationNumber;
}