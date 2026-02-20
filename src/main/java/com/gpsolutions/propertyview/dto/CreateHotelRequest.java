package com.gpsolutions.propertyview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание отеля")
public record CreateHotelRequest(
        @Schema(description = "Название отеля", example = "DoubleTree by Hilton Minsk", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String name,

        @Schema(description = "Описание отеля (опционально)")
        @Size(max = 4096)
        String description,

        @Schema(description = "Бренд отеля", example = "Hilton", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String brand,

        @Schema(description = "Адрес", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Valid
        AddressDto address,

        @Schema(description = "Контакты", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Valid
        ContactsDto contacts,

        @Schema(description = "Время заезда и выезда (опционально)")
        @Valid
        ArrivalTimeDto arrivalTime
) {
}

