package com.gpsolutions.propertyview.dto;

import java.util.UUID;

public record HotelSummaryDto(
        UUID id,
        String name,
        String description,
        String address,
        String phone
) {
}

