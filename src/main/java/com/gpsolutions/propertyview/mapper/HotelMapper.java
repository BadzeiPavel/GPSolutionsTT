package com.gpsolutions.propertyview.mapper;

import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import com.gpsolutions.propertyview.dto.HotelDetailsDto;
import com.gpsolutions.propertyview.dto.HotelSummaryDto;
import com.gpsolutions.propertyview.entity.Address;
import com.gpsolutions.propertyview.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    HotelDetailsDto toDetailsDto(Hotel hotel);

    @Mapping(target = "address", source = "address", qualifiedByName = "addressToString")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelSummaryDto toSummaryDto(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel fromCreateRequest(CreateHotelRequest request);

    @Named("addressToString")
    default String addressToString(Address address) {
        if (address == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (address.getHouseNumber() != null) {
            sb.append(address.getHouseNumber()).append(" ");
        }
        if (address.getStreet() != null) {
            sb.append(address.getStreet());
        }
        if (address.getCity() != null) {
            sb.append(", ").append(address.getCity());
        }
        if (address.getPostCode() != null) {
            sb.append(", ").append(address.getPostCode());
        }
        if (address.getCountry() != null) {
            sb.append(", ").append(address.getCountry());
        }
        return sb.toString();
    }
}

