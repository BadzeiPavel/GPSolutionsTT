package com.gpsolutions.propertyview.service.histogram;

import com.gpsolutions.propertyview.entity.Hotel;
import com.gpsolutions.propertyview.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BrandHistogramStrategy implements HistogramStrategy {

    private final HotelRepository hotelRepository;

    @Override
    public boolean supports(String param) {
        return "brand".equalsIgnoreCase(param);
    }

    @Override
    public Map<String, Long> compute() {
        return hotelRepository.findAll().stream()
                .map(Hotel::getBrand)
                .filter(b -> b != null && !b.isBlank())
                .collect(Collectors.groupingBy(b -> b, Collectors.counting()));
    }
}

