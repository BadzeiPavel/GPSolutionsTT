package com.gpsolutions.propertyview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ArrivalTimeDto(
        @NotBlank
        @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "checkIn must be in HH:mm format")
        String checkIn,

        @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "checkOut must be in HH:mm format")
        String checkOut
) {
}

