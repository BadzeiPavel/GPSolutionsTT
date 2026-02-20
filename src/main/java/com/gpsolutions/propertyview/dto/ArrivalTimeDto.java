package com.gpsolutions.propertyview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Время заезда и выезда")
public record ArrivalTimeDto(
        @Schema(description = "Время заезда (HH:mm)", example = "14:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "checkIn must be in HH:mm format")
        String checkIn,

        @Schema(description = "Время выезда (HH:mm), опционально", example = "12:00", requiredMode = RequiredMode.NOT_REQUIRED)
        @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]$", message = "checkOut must be in HH:mm format")
        String checkOut
) {
}

