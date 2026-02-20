package com.gpsolutions.propertyview.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Краткая информация об отеле")
public record HotelSummaryDto(

        @Schema(description = "Уникальный идентификатор отеля", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Название отеля", example = "DoubleTree by Hilton Minsk")
        String name,

        @Schema(description = "Описание отеля")
        String description,

        @Schema(description = "Адрес одной строкой", example = "9 Pobediteley Avenue, Minsk, 220004, Belarus")
        String address,

        @Schema(description = "Телефон", example = "+375 17 309-80-00")
        String phone
) {
}

