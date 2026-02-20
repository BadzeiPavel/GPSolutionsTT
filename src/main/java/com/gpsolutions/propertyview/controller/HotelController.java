package com.gpsolutions.propertyview.controller;

import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import com.gpsolutions.propertyview.dto.HotelDetailsDto;
import com.gpsolutions.propertyview.dto.HotelSummaryDto;
import com.gpsolutions.propertyview.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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

@Tag(name = "Hotels", description = "API для работы с отелями")
@RequiredArgsConstructor
@RequestMapping("/property-view")
@RestController
public class HotelController {

  private final HotelService hotelService;

  @Operation(summary = "Список отелей", description = "Получение списка всех отелей с краткой информацией")
  @GetMapping("/hotels")
  public ResponseEntity<List<HotelSummaryDto>> getHotels() {
    List<HotelSummaryDto> hotels = hotelService.getAll();
    return ResponseEntity.ok(hotels);
  }

  @Operation(summary = "Отель по ID", description = "Получение расширенной информации по конкретному отелю")
  @GetMapping("/hotels/{id}")
  public ResponseEntity<HotelDetailsDto> getHotelById(
      @Parameter(description = "Идентификатор отеля", example = "1") @PathVariable Long id) {
    HotelDetailsDto hotel = hotelService.getById(id);
    return ResponseEntity.ok(hotel);
  }

  @Operation(summary = "Поиск отелей", description = "Поиск отелей по параметрам: name, brand, city, country, amenities")
  @GetMapping("/search")
  public ResponseEntity<List<HotelSummaryDto>> searchHotels(
      @Parameter(description = "Поиск по названию", example = "Hilton") @RequestParam(required = false) String name,
      @Parameter(description = "Поиск по бренду", example = "Hilton") @RequestParam(required = false) String brand,
      @Parameter(description = "Поиск по городу", example = "Minsk") @RequestParam(required = false) String city,
      @Parameter(description = "Поиск по стране", example = "Belarus") @RequestParam(required = false) String country,
      @Parameter(description = "Поиск по удобствам", example = "Free WiFi") @RequestParam(required = false, name = "amenities") String amenity
  ) {
    List<HotelSummaryDto> hotels = hotelService.search(name, brand, city, country, amenity);
    return ResponseEntity.ok(hotels);
  }

  @Operation(summary = "Создание отеля", description = "Создание нового отеля")
  @PostMapping("/hotels")
  public ResponseEntity<HotelSummaryDto> createHotel(
      @Valid @RequestBody CreateHotelRequest request) {
    HotelSummaryDto body = hotelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @Operation(summary = "Добавление amenities", description = "Добавление списка amenities к отелю")
  @PostMapping("/hotels/{id}/amenities")
  public ResponseEntity<HotelDetailsDto> updateAmenities(
      @Parameter(description = "Идентификатор отеля", example = "1") @PathVariable Long id,
      @Parameter(description = "Список amenities", example = "[\"Free parking\", \"Free WiFi\"]") @RequestBody List<String> amenities
  ) {
    HotelDetailsDto hotel = hotelService.updateAmenities(id, amenities);
    return ResponseEntity.ok(hotel);
  }

  @Operation(summary = "Гистограмма", description = "Количество отелей, сгруппированных по значению параметра (brand, city, country, amenities)")
  @GetMapping("/histogram/{param}")
  public ResponseEntity<Map<String, Long>> histogram(
      @Parameter(description = "Параметр группировки", example = "city") @PathVariable String param) {
    Map<String, Long> result = hotelService.histogram(param);
    return ResponseEntity.ok(result);
  }
}
