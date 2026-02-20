package com.gpsolutions.propertyview.service.impl;

import com.gpsolutions.propertyview.dto.AddressDto;
import com.gpsolutions.propertyview.dto.ContactsDto;
import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import com.gpsolutions.propertyview.dto.HotelDetailsDto;
import com.gpsolutions.propertyview.dto.HotelSummaryDto;
import com.gpsolutions.propertyview.entity.Address;
import com.gpsolutions.propertyview.entity.Hotel;
import com.gpsolutions.propertyview.exception.EntityNotFoundException;
import com.gpsolutions.propertyview.mapper.HotelMapper;
import com.gpsolutions.propertyview.repository.HotelRepository;
import com.gpsolutions.propertyview.service.histogram.HistogramStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private HotelMapper hotelMapper;
    @Mock
    private HistogramStrategy histogramStrategy;

    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new HotelServiceImpl(hotelRepository, hotelMapper, List.of(histogramStrategy));
    }

    @Test
    void getAll_returnsMappedHotels() {
        Hotel hotel = hotel();
        HotelSummaryDto dto = new HotelSummaryDto(1L, "Hotel", null, "addr", "+1");
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(hotelMapper.toSummaryDto(hotel)).thenReturn(dto);

        List<HotelSummaryDto> result = hotelService.getAll();

        assertThat(result).hasSize(1).containsExactly(dto);
        verify(hotelRepository).findAll();
    }

    @Test
    void getById_whenFound_returnsDetails() {
        Hotel hotel = hotel();
        HotelDetailsDto dto = new HotelDetailsDto(1L, "Hotel", null, "Brand", null, null, null, List.of());
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailsDto(hotel)).thenReturn(dto);

        HotelDetailsDto result = hotelService.getById(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getById_whenNotFound_throws() {
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hotel not found");
    }

    @Test
    void search_returnsMappedHotels() {
        Hotel hotel = hotel();
        HotelSummaryDto dto = new HotelSummaryDto(1L, "Hotel", null, "addr", "+1");
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(List.of(hotel));
        when(hotelMapper.toSummaryDto(hotel)).thenReturn(dto);

        List<HotelSummaryDto> result = hotelService.search("Hilton", null, null, null, null);

        assertThat(result).hasSize(1).containsExactly(dto);
    }

    @Test
    void create_savesAndReturnsSummary() {
        CreateHotelRequest request = createRequest();
        Hotel hotel = hotel();
        Hotel saved = hotel();
        saved.setId(1L);
        HotelSummaryDto dto = new HotelSummaryDto(1L, "Hotel", null, "addr", "+1");
        when(hotelMapper.fromCreateRequest(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(saved);
        when(hotelMapper.toSummaryDto(saved)).thenReturn(dto);

        HotelSummaryDto result = hotelService.create(request);

        assertThat(result).isEqualTo(dto);
        verify(hotelRepository).save(hotel);
    }

    @Test
    void updateAmenities_whenFound_updatesAndReturnsDetails() {
        Hotel hotel = hotel();
        hotel.setAmenities(new java.util.ArrayList<>());
        HotelDetailsDto dto = new HotelDetailsDto(1L, "Hotel", null, "Brand", null, null, null, List.of("WiFi"));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailsDto(hotel)).thenReturn(dto);

        HotelDetailsDto result = hotelService.updateAmenities(1L, List.of("WiFi"));

        assertThat(result).isEqualTo(dto);
        assertThat(hotel.getAmenities()).containsExactly("WiFi");
    }

    @Test
    void updateAmenities_whenNotFound_throws() {
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.updateAmenities(999L, List.of("WiFi")))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hotel not found");
    }

    @Test
    void histogram_whenStrategySupports_returnsResult() {
        Map<String, Long> expected = Map.of("Minsk", 2L);
        when(histogramStrategy.supports("city")).thenReturn(true);
        when(histogramStrategy.compute()).thenReturn(expected);

        Map<String, Long> result = hotelService.histogram("city");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void histogram_whenNoStrategySupports_throws() {
        when(histogramStrategy.supports(any())).thenReturn(false);

        assertThatThrownBy(() -> hotelService.histogram("invalid"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Unsupported histogram parameter");
    }

    private Hotel hotel() {
        return Hotel.builder()
                .id(1L)
                .name("Hotel")
                .brand("Hilton")
                .address(Address.builder().houseNumber("9").street("Street").city("Minsk").country("Belarus").postCode("220004").build())
                .build();
    }

    private CreateHotelRequest createRequest() {
        AddressDto address = new AddressDto("9", "Street", "Minsk", "Belarus", "220004");
        ContactsDto contacts = new ContactsDto("+1", "a@b.com");
        return new CreateHotelRequest("Hotel", null, "Hilton", address, contacts, null);
    }
}
