package com.gpsolutions.propertyview.service.histogram;

import com.gpsolutions.propertyview.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AmenitiesHistogramStrategy implements HistogramStrategy {

    private final HotelRepository hotelRepository;

    @Override
    public boolean supports(String param) {
        return "amenities".equalsIgnoreCase(param);
    }

    @Override
    public Map<String, Long> compute() {
        return hotelRepository.findAll().stream()
                .map(h -> h.getAmenities() != null ? h.getAmenities() : java.util.List.<String>of())
                .flatMap(Collection::stream)
                .filter(a -> a != null && !a.isBlank())
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()));
    }
}

