package com.gpsolutions.propertyview.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Ответ об ошибке")
public record ErrorResponseDto(

        @Schema(description = "Время возникновения ошибки")
        Instant timestamp,

        @Schema(description = "HTTP статус код")
        int status,

        @Schema(description = "Описание статуса")
        String error,

        @Schema(description = "Код ошибки")
        String code,

        @Schema(description = "Сообщение об ошибке")
        String message,

        @Schema(description = "Путь запроса")
        String path
) {
}

