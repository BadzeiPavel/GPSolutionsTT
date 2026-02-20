package com.gpsolutions.propertyview.specification;

import com.gpsolutions.propertyview.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;

public class HotelSpecifications {

  public static Specification<Hotel> nameContains(String name) {
    return (root, query, cb) ->
        cb.like(cb.lower(root.get("name")),
            "%" + name.toLowerCase() + "%");
  }

  public static Specification<Hotel> brandContains(String brand) {
    return (root, query, cb) ->
        cb.like(cb.lower(root.get("brand")),
            "%" + brand.toLowerCase() + "%");
  }

  public static Specification<Hotel> cityContains(String city) {
    return (root, query, cb) ->
        cb.like(cb.lower(root.get("address").get("city")),
            "%" + city.toLowerCase() + "%");
  }

  public static Specification<Hotel> countryContains(String country) {
    return (root, query, cb) ->
        cb.like(cb.lower(root.get("address").get("country")),
            "%" + country.toLowerCase() + "%");
  }

  public static Specification<Hotel> amenityContains(String amenity) {
    return (root, query, cb) -> {
      query.distinct(true);
      return cb.like(
          cb.lower(root.join("amenities")),
          "%" + amenity.toLowerCase() + "%"
      );
    };
  }
}