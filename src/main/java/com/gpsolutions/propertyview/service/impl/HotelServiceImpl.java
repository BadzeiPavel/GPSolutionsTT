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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    public HotelDetailsDto getById(UUID id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        HttpStatus.NOT_FOUND,
                        "HOTEL_NOT_FOUND",
                        "Hotel not found: " + id
                ));
        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    public List<HotelSummaryDto> search(String name, String brand, String city, String country, String amenity) {
        boolean hasAnyFilter = isNotBlank(name) || isNotBlank(brand) || isNotBlank(city) 
                || isNotBlank(country) || isNotBlank(amenity);
        
        if (!hasAnyFilter) {
            return hotelRepository.findAll().stream()
                    .map(hotelMapper::toSummaryDto)
                    .toList();
        }

        Set<Hotel> filteredHotels = null;
        
        if (isNotBlank(name)) {
            filteredHotels = new LinkedHashSet<>(hotelRepository.findByNameContainingIgnoreCase(name));
        }
        if (isNotBlank(brand)) {
            List<Hotel> brandMatches = hotelRepository.findByBrandContainingIgnoreCase(brand);
            filteredHotels = filteredHotels == null 
                    ? new LinkedHashSet<>(brandMatches)
                    : intersect(filteredHotels, brandMatches);
        }
        if (isNotBlank(city)) {
            List<Hotel> cityMatches = hotelRepository.findByAddress_CityContainingIgnoreCase(city);
            filteredHotels = filteredHotels == null 
                    ? new LinkedHashSet<>(cityMatches)
                    : intersect(filteredHotels, cityMatches);
        }
        if (isNotBlank(country)) {
            List<Hotel> countryMatches = hotelRepository.findByAddress_CountryContainingIgnoreCase(country);
            filteredHotels = filteredHotels == null 
                    ? new LinkedHashSet<>(countryMatches)
                    : intersect(filteredHotels, countryMatches);
        }
        if (isNotBlank(amenity)) {
            List<Hotel> amenityMatches = hotelRepository.findDistinctByAmenitiesIgnoreCaseContaining(amenity);
            filteredHotels = filteredHotels == null 
                    ? new LinkedHashSet<>(amenityMatches)
                    : intersect(filteredHotels, amenityMatches);
        }

        return filteredHotels.stream()
                .map(hotelMapper::toSummaryDto)
                .toList();
    }
    
    private Set<Hotel> intersect(Set<Hotel> current, List<Hotel> next) {
        current.retainAll(next);
        return current;
    }
    
    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
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
    public HotelDetailsDto updateAmenities(UUID id, List<String> amenities) {
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


