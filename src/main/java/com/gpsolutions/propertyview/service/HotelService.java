package com.gpsolutions.propertyview.service;

import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import com.gpsolutions.propertyview.dto.HotelDetailsDto;
import com.gpsolutions.propertyview.dto.HotelSummaryDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface HotelService {

    List<HotelSummaryDto> getAll();

    HotelDetailsDto getById(UUID id);

    List<HotelSummaryDto> search(String name, String brand, String city, String country, String amenity);

    HotelSummaryDto create(CreateHotelRequest request);

    HotelDetailsDto updateAmenities(UUID id, List<String> amenities);

    Map<String, Long> histogram(String param);
}

