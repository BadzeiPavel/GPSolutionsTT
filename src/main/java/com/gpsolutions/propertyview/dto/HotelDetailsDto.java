package com.gpsolutions.propertyview.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Расширенная информация об отеле")
public record HotelDetailsDto(

        @Schema(description = "Уникальный идентификатор отеля", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Название отеля", example = "DoubleTree by Hilton Minsk")
        String name,

        @Schema(description = "Описание отеля")
        String description,

        @Schema(description = "Бренд отеля", example = "Hilton")
        String brand,

        @Schema(description = "Адрес")
        AddressDto address,

        @Schema(description = "Контакты")
        ContactsDto contacts,

        @Schema(description = "Время заезда и выезда")
        ArrivalTimeDto arrivalTime,

        @Schema(description = "Список удобств")
        List<String> amenities
) {
}

