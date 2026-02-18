package com.gpsolutions.propertyview.service.histogram;

import com.gpsolutions.propertyview.entity.Hotel;
import com.gpsolutions.propertyview.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CountryHistogramStrategy implements HistogramStrategy {

    private final HotelRepository hotelRepository;

    @Override
    public boolean supports(String param) {
        return "country".equalsIgnoreCase(param);
    }

    @Override
    public Map<String, Long> compute() {
        return hotelRepository.findAll().stream()
                .map(h -> h.getAddress() != null ? h.getAddress().getCountry() : null)
                .filter(c -> c != null && !c.isBlank())
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }
}

