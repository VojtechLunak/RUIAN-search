package cz.cvut.fit.ruiansearch.controller;

import cz.cvut.fit.ruiansearch.model.Address;
import cz.cvut.fit.ruiansearch.service.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/suggestions")
public class SuggestionController {
    private final AddressService addressService;

    public SuggestionController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/city")
    public List<String> getCitySuggestions(@RequestParam String city) {
        if (isEmptyOrNull(city)) {
            return Collections.emptyList();
        }

        GroupResult<Address> groupResult = addressService.getCitySuggestions(city);

        return getGroupValues(groupResult);
    }

    @GetMapping("/district")
    public List<String> getDistrictSuggestions(
        @RequestParam(defaultValue = "*") String city,
        @RequestParam(defaultValue = "*") String district) {
        if (city.equals("*") && district.equals("*")) {
            return Collections.emptyList();
        }

        GroupResult<Address> groupResult = addressService.getDistrictSuggestions(city, district);

        return getGroupValues(groupResult);
    }

    @GetMapping("/street")
    public List<String> getStreetSuggestions(
            @RequestParam(defaultValue = "*") String city,
            @RequestParam(defaultValue = "*") String district,
            @RequestParam(defaultValue = "*") String street) {
        if (city.equals("*") && street.equals("*")) {
            return Collections.emptyList();
        }

        GroupResult<Address> groupResult = addressService.getStreetSuggestions(city, district, street);

        return getGroupValues(groupResult);

    }

    @GetMapping("/houseNumber")
    public List<String> gethouseNumberSuggestions(
            @RequestParam(defaultValue = "*") String city,
            @RequestParam(defaultValue = "*") String district,
            @RequestParam(defaultValue = "*") String street,
            @RequestParam(defaultValue = "*") String houseNumber) {
        if (city.equals("*") && street.equals("*") && houseNumber.equals("*")) {
            return Collections.emptyList();
        }

        GroupResult<Address> groupResult = addressService.getHouseNumberSuggestions(city, district, street, houseNumber);
        List<GroupEntry<Address>> groupEntries = groupResult.getGroupEntries().getContent();
        List<String> suggestions = groupEntries.stream()
                .map(GroupEntry::getGroupValue)
                .sorted(Comparator.comparing(x -> Integer.parseInt(x.split("/")[0])))
                .collect(Collectors.toList());

        return suggestions;
    }

    @GetMapping("/suggest")
    public List<Address> getSuggestions(
            @RequestParam String searchString) {
        if (searchString.length() <= 3) {
            return Collections.emptyList();
        }

        Page<Address> groupResult = addressService.getSuggestions(searchString);

        return groupResult.toList();
    }


    private boolean isEmptyOrNull(String input) {
        return (input == null || input == "");
    }

    private List<String> getGroupValues(GroupResult<Address> groupResult) {
        List<GroupEntry<Address>> groupEntries = groupResult.getGroupEntries().getContent();
        List<String> result = groupEntries.stream()
                .sorted(Comparator.comparing( (GroupEntry<Address> x) ->
                        x.getResult().getTotalElements()).reversed())
                .map(GroupEntry::getGroupValue)
                .collect(Collectors.toList());

        return result;
    }

}
