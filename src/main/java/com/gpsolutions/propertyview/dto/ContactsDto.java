package com.gpsolutions.propertyview.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactsDto(
        @NotBlank
        @Pattern(
                regexp = "^\\+?\\d{1,3}[0-9\\- ()]{4,22}$",
                message = "phone must be a valid international phone number with 1-3 digit country code"
        )
        String phone,

        @NotBlank
        @Email
        @Size(max = 255)
        String email
) {
}

