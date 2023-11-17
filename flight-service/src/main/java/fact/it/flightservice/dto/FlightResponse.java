package fact.it.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {
    private String flightNumber;
    private boolean isDepartingFlight;
    private String remoteAirportCode;
    private Date scheduledTime;
    private String gateNumber;
    private String registrationNumber;
}