package com.restfulbooker.api.dtos.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import static com.restfulbooker.constants.GeneralConstants.DATE_TIME_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class BookingDatesDTO {
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDate checkin;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDate checkout;
}
