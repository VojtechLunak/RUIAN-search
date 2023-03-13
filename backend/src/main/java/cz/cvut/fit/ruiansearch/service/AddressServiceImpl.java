package cz.cvut.fit.ruiansearch.service;

import cz.cvut.fit.ruiansearch.model.Address;
import cz.cvut.fit.ruiansearch.repository.AddressCustomRepository;
import cz.cvut.fit.ruiansearch.repository.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.geo.Point;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressCustomRepository addressCustomRepository;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            AddressCustomRepository addressCustomRepository) {
        this.addressRepository = addressRepository;
        this.addressCustomRepository = addressCustomRepository;
    }

    @Override
    public Page<Address> search(
            String city,
            String district,
            String street,
            String houseNumber,
            Pageable pageable) {
        return addressRepository.search(
                city, district, street, houseNumber, pageable);
    }

    @Override
    public Page<Address> searchWithEmptyStreet(
            String city,
            String district,
            String houseNumber,
            Pageable pageable) {
        return addressRepository.searchWithEmptyStreet(
                city, district, houseNumber, pageable);
    }

    @Override
    public Page<Address> findByAdmCodeStartsWith(String admCode, Pageable pageable) {
        return addressRepository.findByAdmCodeStartsWith(admCode, pageable);
    }

    @Override
    public Optional<Address> findByAdmCode(Integer admCode) {
        return addressRepository.findByAdmCodeEquals(admCode);
    }

    @Override
    public GroupResult<Address> getCitySuggestions(String city) {
        return addressCustomRepository.getCitySuggestions(city);
    }

    @Override
    public GroupResult<Address> getDistrictSuggestions(String city, String district) {
        return addressCustomRepository.getDistrictSuggestions(city, district);
    }

    @Override
    public GroupResult<Address> getStreetSuggestions(String city, String district, String street) {
        return addressCustomRepository.getStreetSuggestions(city, district, street);
    }

    @Override
    public GroupResult<Address> getHouseNumberSuggestions(
            String city,
            String district,
            String street,
            String houseNumber) {
        return addressCustomRepository.getHouseNumberSuggestions(city, district, street, houseNumber);
    }

    @Override
    public Page<Address> getSuggestions(String searchString) {
        return addressCustomRepository.getSuggestions(searchString);
    }

    @Override
    public Page<Address> getNearbyAddresses(Point center, Double distance) {
        return addressCustomRepository.findNearbyAddresses(center, distance);
    }
}
