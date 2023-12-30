package fact.it.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDto {
    private String firstName;
    private String lastName;
    private String nationality;
    private Date birthDate;
}
