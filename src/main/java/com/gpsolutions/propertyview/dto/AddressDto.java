package com.gpsolutions.propertyview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotBlank
        @Pattern(regexp = "\\d+", message = "houseNumber must be numeric")
        String houseNumber,

        @NotBlank
        @Size(max = 255)
        String street,

        @NotBlank
        @Size(max = 255)
        String city,

        @NotBlank
        @Size(max = 255)
        String country,

        @NotBlank
        @Size(max = 50)
        String postCode
) {
}

