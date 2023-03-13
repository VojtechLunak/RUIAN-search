package cz.cvut.fit.ruiansearch.service;

import cz.cvut.fit.ruiansearch.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.geo.Point;
import org.springframework.data.solr.core.query.result.GroupResult;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    Page<Address> search(
            String city,
            String district,
            String street,
            String houseNumber,
            Pageable pageable);
    Page<Address> searchWithEmptyStreet(
        String city,
        String district,
        String houseNumber,
        Pageable pageable);
    Page<Address> findByAdmCodeStartsWith(String admCode, Pageable pageable);
    Optional<Address> findByAdmCode(Integer admCode);
    GroupResult<Address> getCitySuggestions(String city);
    GroupResult<Address> getDistrictSuggestions(String city, String district);
    GroupResult<Address> getStreetSuggestions(String city, String district, String street);
    GroupResult<Address> getHouseNumberSuggestions(String city, String district, String street, String houseNumber);
    Page<Address> getSuggestions(String searchString);
    Page<Address> getNearbyAddresses(Point center, Double distance);
}
