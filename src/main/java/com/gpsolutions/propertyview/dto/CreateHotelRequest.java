package com.gpsolutions.propertyview.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHotelRequest(
        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 4096)
        String description,

        @NotBlank
        @Size(max = 255)
        String brand,

        @NotNull
        @Valid
        AddressDto address,

        @NotNull
        @Valid
        ContactsDto contacts,

        @Valid
        ArrivalTimeDto arrivalTime
) {
}

