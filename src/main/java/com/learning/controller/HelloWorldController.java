package com.learning.controller;

import com.learning.dto.CountryRequest;
import com.learning.dto.CountryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/country")
public class HelloWorldController {

    private List<CountryResponse> countries = insertCountries();

    private List<CountryResponse> insertCountries() {
        var listOfCountries = new ArrayList<CountryResponse>();
        listOfCountries.add(new CountryResponse(1, "Brazil", 215_000_000L));
        listOfCountries.add(new CountryResponse(2, "China", 1_400_000_000L));
        listOfCountries.add(new CountryResponse(3, "Alemanha", 83_000_000L));
        listOfCountries.add(new CountryResponse(4, "Argentina", 45_000_000L));
        return listOfCountries;
    }

    //CREATE - POST
    @PostMapping
    public ResponseEntity<CountryResponse> save(@RequestBody final CountryRequest request) {

        Integer id = countries.size();
        var response = new CountryResponse(id, request.getName(), request.getPopulation());

        countries.add(response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    //READ - GET /country
    //- read all
    //@GetMapping
    public ResponseEntity<List<CountryResponse>> getAll() {
        return ResponseEntity.ok(countries);
    }

    //- read specific GET /country/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> findById(@PathVariable("id") final long id) {

        for(var country: countries) {
            if (country.getId() == id) {
                return ResponseEntity.ok(country);
            }
        }

        return ResponseEntity.notFound().build();
    }

    //- filtering - GET /country - ambiguous
    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAll(
            @RequestParam(name = "prefix", required = false) final String prefix){

        if (Objects.isNull(prefix)) {
            return ResponseEntity.ok(countries);
        } else {
            var listOfCountries =
                    countries.stream()
                            .filter(countryDto -> countryDto.getName().startsWith(prefix))
                            .collect(Collectors.toList());
            return ResponseEntity.ok(listOfCountries);
        }
    }

    //UPDATE - PUT / PATCH country/{id}/
    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> update(@PathVariable("id") final int id,
                                                  @RequestBody final CountryRequest request) {
        //find
        CountryResponse countryDto = null;
        for(var country: countries) {
            if (country.getId() == id) {
                countryDto = country;
            }
        }
        //update
        if (Objects.nonNull(countryDto)) {
            countryDto.setName(request.getName());
            countryDto.setPopulation(request.getPopulation());
            return ResponseEntity.ok(countryDto);
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    //DELETE - DELETE /{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final int id) {

        //find
        int index = -1;
        for (int i = 0; i < countries.size(); i++) {
            if(countries.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        //remove
        if (index >= 0) {
            countries.remove(index);
        }
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

}
