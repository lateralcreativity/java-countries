package com.lambdaschool.countries.controllers;

import com.lambdaschool.countries.models.Country;
import com.lambdaschool.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController
{
    @Autowired
    CountryRepository countryrepo;

    private List<Country> findCountry(List<Country> myList, CheckCountry tester)
    {
        List<Country> tempList = new ArrayList<>();

        for(Country c : myList)
        {
            if(tester.test(c))
            {
                tempList.add(c);
            }
        }

        return tempList;
    }

    // http://localhost:2019/names/all
    // return the names of all the countries alphabetically
    @GetMapping(value = "/names/all", produces = {"application/json"})
    public ResponseEntity<?> listAllCountries()
    {
        List<Country> myList = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(myList::add);

        myList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    // http://localhost:2019/names/start/u
    // return the countries alphabetically that begin with the given letter
    @GetMapping(value="/names/start/{letter}", produces = {"application/json"})
    public ResponseEntity<?> listAllByFirstLetter(@PathVariable char letter)
    {
        List<Country> myList = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(myList::add);
        List<Country> rtnList = findCountry(myList, c -> c.getName().charAt(0) == Character.toUpperCase(letter));
        rtnList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // http://localhost:2019/population/total
    // return the total population of the all countries in the console while returning Http Status OK as the response
    @GetMapping(value="/population/total", produces = {"application/json"})
    public ResponseEntity<?> getPopulationTotal()
    {
        List<Country> myList = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(myList::add);
        long sum = myList.stream().mapToLong(c -> c.getPopulation()).sum();
        System.out.println("The total population is " + sum);

        return new ResponseEntity<>("HttpStatus.OK", HttpStatus.OK);
    }

    // http://localhost:2019/population/min
    // return the country with the smallest population
    @GetMapping(value="/population/min", produces = {"application/json"})
    public ResponseEntity<?> getPopulationMin()
    {
        List<Country> myList = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1, c2) -> (int)(c1.getPopulation() - c2.getPopulation()));

        return new ResponseEntity<>(myList.get(0), HttpStatus.OK);
    }

    // http://localhost:2019/population/max
    // return the country with the largest population
    @GetMapping(value="/population/max", produces = {"application/json"})
    public ResponseEntity<?> getPopulationMax()
    {
        List<Country> myList = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1, c2) -> (int)(c2.getPopulation() - c1.getPopulation()));

        return new ResponseEntity<>(myList.get(0), HttpStatus.OK);
    }
}
