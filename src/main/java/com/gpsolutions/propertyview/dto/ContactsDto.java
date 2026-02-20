package com.gpsolutions.propertyview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Контакты")
public record ContactsDto(
        @Schema(description = "Телефон (международный формат)", example = "+375 17 309-80-00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String phone,

        @Schema(description = "Email", example = "example@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Email
        @Size(max = 255)
        String email
) {
}

