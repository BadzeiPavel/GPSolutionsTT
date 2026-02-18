package com.gpsolutions.propertyview.repository;

import com.gpsolutions.propertyview.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {

    List<Hotel> findByNameContainingIgnoreCase(String name);

    List<Hotel> findByBrandContainingIgnoreCase(String brand);

    List<Hotel> findByAddress_CityContainingIgnoreCase(String city);

    List<Hotel> findByAddress_CountryContainingIgnoreCase(String country);

    @Query("""
       SELECT DISTINCT h
       FROM Hotel h
       JOIN h.amenities a
       WHERE LOWER(a) LIKE LOWER(CONCAT('%', :amenity, '%'))
       """)
    List<Hotel> findDistinctByAmenitiesIgnoreCaseContaining(String amenity);

}

