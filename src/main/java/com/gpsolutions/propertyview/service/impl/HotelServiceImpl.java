package com.gpsolutions.propertyview.service.impl;

import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import com.gpsolutions.propertyview.dto.HotelDetailsDto;
import com.gpsolutions.propertyview.dto.HotelSummaryDto;
import com.gpsolutions.propertyview.entity.Hotel;
import com.gpsolutions.propertyview.exception.EntityNotFoundException;
import com.gpsolutions.propertyview.mapper.HotelMapper;
import com.gpsolutions.propertyview.repository.HotelRepository;
import com.gpsolutions.propertyview.service.HotelService;
import com.gpsolutions.propertyview.service.histogram.HistogramStrategy;
import com.gpsolutions.propertyview.specification.HotelSpecifications;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final List<HistogramStrategy> histogramStrategies;

    @Override
    public List<HotelSummaryDto> getAll() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toSummaryDto)
                .toList();
    }

    @Override
    public HotelDetailsDto getById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        HttpStatus.NOT_FOUND,
                        "HOTEL_NOT_FOUND",
                        "Hotel not found: " + id
                ));
        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    public List<HotelSummaryDto> search(
        String name,
        String brand,
        String city,
        String country,
        String amenity) {

        Specification<Hotel> spec = Specification.where(null);

        if (name != null && !name.isBlank()) {
            spec = spec.and(HotelSpecifications.nameContains(name.trim()));
        }
        if (brand != null && !brand.isBlank()) {
            spec = spec.and(HotelSpecifications.brandContains(brand.trim()));
        }
        if (city != null && !city.isBlank()) {
            spec = spec.and(HotelSpecifications.cityContains(city.trim()));
        }
        if (country != null && !country.isBlank()) {
            spec = spec.and(HotelSpecifications.countryContains(country.trim()));
        }
        if (amenity != null && !amenity.isBlank()) {
            spec = spec.and(HotelSpecifications.amenityContains(amenity.trim()));
        }

        return hotelRepository.findAll(spec)
            .stream()
            .map(hotelMapper::toSummaryDto)
            .toList();
    }

    @Override
    @Transactional
    public HotelSummaryDto create(CreateHotelRequest request) {
        Hotel hotel = hotelMapper.fromCreateRequest(request);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toSummaryDto(saved);
    }

    @Override
    @Transactional
    public HotelDetailsDto updateAmenities(Long id, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        HttpStatus.NOT_FOUND,
                        "HOTEL_NOT_FOUND",
                        "Hotel not found: " + id
                ));
        List<String> normalized = Optional.ofNullable(amenities)
                .orElse(List.of())
                .stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .toList();
        hotel.setAmenities(normalized);
        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    public Map<String, Long> histogram(String param) {
        return histogramStrategies.stream()
                .filter(s -> s.supports(param))
                .findFirst()
                .map(HistogramStrategy::compute)
                .orElseThrow(() -> new EntityNotFoundException(
                        HttpStatus.BAD_REQUEST,
                        "UNSUPPORTED_HISTOGRAM_PARAM",
                        "Unsupported histogram parameter: " + param
                ));
    }

}


