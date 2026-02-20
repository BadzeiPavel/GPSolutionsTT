package com.gpsolutions.propertyview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Адрес")
public record AddressDto(
        @Schema(description = "Номер дома", example = "9", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "\\d+", message = "houseNumber must be numeric")
        String houseNumber,

        @Schema(description = "Улица", example = "Pobediteley Avenue", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String street,

        @Schema(description = "Город", example = "Minsk", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String city,

        @Schema(description = "Страна", example = "Belarus", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String country,

        @Schema(description = "Почтовый индекс", example = "110011", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 50)
        String postCode
) {
}

