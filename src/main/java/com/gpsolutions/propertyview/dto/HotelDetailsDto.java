package com.gpsolutions.propertyview.dto;

import java.util.List;
import java.util.UUID;

public record HotelDetailsDto(
        UUID id,
        String name,
        String description,
        String brand,
        AddressDto address,
        ContactsDto contacts,
        ArrivalTimeDto arrivalTime,
        List<String> amenities
) {
}

