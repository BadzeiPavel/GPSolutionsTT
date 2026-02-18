package com.gpsolutions.propertyview.dto;

import java.time.Instant;

public record ErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        String code,
        String message,
        String path
) {
}

