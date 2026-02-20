package com.gpsolutions.propertyview.service;

import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import com.gpsolutions.propertyview.dto.HotelDetailsDto;
import com.gpsolutions.propertyview.dto.HotelSummaryDto;

import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelSummaryDto> getAll();

    HotelDetailsDto getById(Long id);

    List<HotelSummaryDto> search(String name, String brand, String city, String country, String amenity);

    HotelSummaryDto create(CreateHotelRequest request);

    HotelDetailsDto updateAmenities(Long id, List<String> amenities);

    Map<String, Long> histogram(String param);
}

