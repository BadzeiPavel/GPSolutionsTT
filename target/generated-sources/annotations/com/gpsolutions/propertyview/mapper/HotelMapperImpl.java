package com.gpsolutions.propertyview.mapper;

import com.gpsolutions.propertyview.dto.AddressDto;
import com.gpsolutions.propertyview.dto.ArrivalTimeDto;
import com.gpsolutions.propertyview.dto.ContactsDto;
import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import com.gpsolutions.propertyview.dto.HotelDetailsDto;
import com.gpsolutions.propertyview.dto.HotelSummaryDto;
import com.gpsolutions.propertyview.entity.Address;
import com.gpsolutions.propertyview.entity.ArrivalTime;
import com.gpsolutions.propertyview.entity.Contacts;
import com.gpsolutions.propertyview.entity.Hotel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-18T20:36:01+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Amazon.com Inc.)"
)
@Component
public class HotelMapperImpl implements HotelMapper {

    @Override
    public HotelDetailsDto toDetailsDto(Hotel hotel) {
        if ( hotel == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        String brand = null;
        AddressDto address = null;
        ContactsDto contacts = null;
        ArrivalTimeDto arrivalTime = null;
        List<String> amenities = null;

        id = hotel.getId();
        name = hotel.getName();
        description = hotel.getDescription();
        brand = hotel.getBrand();
        address = addressToAddressDto( hotel.getAddress() );
        contacts = contactsToContactsDto( hotel.getContacts() );
        arrivalTime = arrivalTimeToArrivalTimeDto( hotel.getArrivalTime() );
        List<String> list = hotel.getAmenities();
        if ( list != null ) {
            amenities = new ArrayList<String>( list );
        }

        HotelDetailsDto hotelDetailsDto = new HotelDetailsDto( id, name, description, brand, address, contacts, arrivalTime, amenities );

        return hotelDetailsDto;
    }

    @Override
    public HotelSummaryDto toSummaryDto(Hotel hotel) {
        if ( hotel == null ) {
            return null;
        }

        String address = null;
        String phone = null;
        UUID id = null;
        String name = null;
        String description = null;

        address = addressToString( hotel.getAddress() );
        phone = hotelContactsPhone( hotel );
        id = hotel.getId();
        name = hotel.getName();
        description = hotel.getDescription();

        HotelSummaryDto hotelSummaryDto = new HotelSummaryDto( id, name, description, address, phone );

        return hotelSummaryDto;
    }

    @Override
    public Hotel fromCreateRequest(CreateHotelRequest request) {
        if ( request == null ) {
            return null;
        }

        Hotel.HotelBuilder hotel = Hotel.builder();

        hotel.name( request.name() );
        hotel.description( request.description() );
        hotel.brand( request.brand() );
        hotel.address( addressDtoToAddress( request.address() ) );
        hotel.contacts( contactsDtoToContacts( request.contacts() ) );
        hotel.arrivalTime( arrivalTimeDtoToArrivalTime( request.arrivalTime() ) );

        return hotel.build();
    }

    protected AddressDto addressToAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        String houseNumber = null;
        String street = null;
        String city = null;
        String country = null;
        String postCode = null;

        houseNumber = address.getHouseNumber();
        street = address.getStreet();
        city = address.getCity();
        country = address.getCountry();
        postCode = address.getPostCode();

        AddressDto addressDto = new AddressDto( houseNumber, street, city, country, postCode );

        return addressDto;
    }

    protected ContactsDto contactsToContactsDto(Contacts contacts) {
        if ( contacts == null ) {
            return null;
        }

        String phone = null;
        String email = null;

        phone = contacts.getPhone();
        email = contacts.getEmail();

        ContactsDto contactsDto = new ContactsDto( phone, email );

        return contactsDto;
    }

    protected ArrivalTimeDto arrivalTimeToArrivalTimeDto(ArrivalTime arrivalTime) {
        if ( arrivalTime == null ) {
            return null;
        }

        String checkIn = null;
        String checkOut = null;

        checkIn = arrivalTime.getCheckIn();
        checkOut = arrivalTime.getCheckOut();

        ArrivalTimeDto arrivalTimeDto = new ArrivalTimeDto( checkIn, checkOut );

        return arrivalTimeDto;
    }

    private String hotelContactsPhone(Hotel hotel) {
        if ( hotel == null ) {
            return null;
        }
        Contacts contacts = hotel.getContacts();
        if ( contacts == null ) {
            return null;
        }
        String phone = contacts.getPhone();
        if ( phone == null ) {
            return null;
        }
        return phone;
    }

    protected Address addressDtoToAddress(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.houseNumber( addressDto.houseNumber() );
        address.street( addressDto.street() );
        address.city( addressDto.city() );
        address.country( addressDto.country() );
        address.postCode( addressDto.postCode() );

        return address.build();
    }

    protected Contacts contactsDtoToContacts(ContactsDto contactsDto) {
        if ( contactsDto == null ) {
            return null;
        }

        Contacts.ContactsBuilder contacts = Contacts.builder();

        contacts.phone( contactsDto.phone() );
        contacts.email( contactsDto.email() );

        return contacts.build();
    }

    protected ArrivalTime arrivalTimeDtoToArrivalTime(ArrivalTimeDto arrivalTimeDto) {
        if ( arrivalTimeDto == null ) {
            return null;
        }

        ArrivalTime.ArrivalTimeBuilder arrivalTime = ArrivalTime.builder();

        arrivalTime.checkIn( arrivalTimeDto.checkIn() );
        arrivalTime.checkOut( arrivalTimeDto.checkOut() );

        return arrivalTime.build();
    }
}
