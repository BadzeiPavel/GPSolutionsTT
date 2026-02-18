package com.gpsolutions.propertyview.controller;

import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import com.gpsolutions.propertyview.dto.HotelDetailsDto;
import com.gpsolutions.propertyview.dto.HotelSummaryDto;
import com.gpsolutions.propertyview.service.HotelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

  private final HotelService hotelService;

  @GetMapping("/hotels")
  public ResponseEntity<List<HotelSummaryDto>> getHotels() {
    List<HotelSummaryDto> hotels = hotelService.getAll();
    return ResponseEntity.ok(hotels);
  }

  @GetMapping("/hotels/{id}")
  public ResponseEntity<HotelDetailsDto> getHotelById(@PathVariable UUID id) {
    HotelDetailsDto hotel = hotelService.getById(id);
    return ResponseEntity.ok(hotel);
  }

  @GetMapping("/search")
  public ResponseEntity<List<HotelSummaryDto>> searchHotels(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String country,
      @RequestParam(required = false, name = "amenities") String amenity
  ) {
    List<HotelSummaryDto> hotels = hotelService.search(name, brand, city, country, amenity);
    return ResponseEntity.ok(hotels);
  }

  @PostMapping("/hotels")
  public ResponseEntity<HotelSummaryDto> createHotel(
      @Valid @RequestBody CreateHotelRequest request) {
    HotelSummaryDto body = hotelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @PostMapping("/hotels/{id}/amenities")
  public ResponseEntity<HotelDetailsDto> updateAmenities(
      @PathVariable UUID id,
      @RequestBody List<String> amenities
  ) {
    HotelDetailsDto hotel = hotelService.updateAmenities(id, amenities);
    return ResponseEntity.ok(hotel);
  }

  @GetMapping("/histogram/{param}")
  public ResponseEntity<Map<String, Long>> histogram(@PathVariable String param) {
    Map<String, Long> result = hotelService.histogram(param);
    return ResponseEntity.ok(result);
  }
}
