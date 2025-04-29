package com.airline.authservice.service.impl;

import com.airline.authservice.dto.City;
import com.airline.authservice.dto.FlightInformationDto;
import com.airline.authservice.exception.NotFoundException;
import com.airline.authservice.model.*;
import com.airline.authservice.repository.FlightRepository;
import com.airline.authservice.service.FlightService;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static com.airline.authservice.mapper.FlightInformationMapper.*;

@Service
public class FlightServiceImpl implements FlightService {
//    @Value("${external.api.url}")
//    private String externalApiUrl;
    private final String productServiceUrl = "http://product:9090/product/"; // URL of the ProductService
    private final RestTemplate restTemplate;

    private final FlightRepository flightRepository;


    public FlightServiceImpl(RestTemplate restTemplate, FlightRepository flightRepository) {
        this.restTemplate = restTemplate;
        this.flightRepository = flightRepository;
    }


//
//
//    @Override
//    @Cacheable(value = "flightDataCache")
//    public List<Country> getCountries(String accessKey,int offset, int limit) {
//        // Pozivanje eksternog API-ja
//        String url = externalApiUrl + "/countries?access_key=" + accessKey + "&offset=" + offset + "&limit=" + limit;
//
//        // Uporaba generičkog ApiResponse sa specifičnim tipom Country
//        ParameterizedTypeReference<ApiResponse<Country>> responseType = new ParameterizedTypeReference<>() {};
//        ResponseEntity<ApiResponse<Country>> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                null,
//                responseType
//        );
//
//        ApiResponse<Country> apiResponse = responseEntity.getBody();
//        if (apiResponse != null && apiResponse.getData() != null) {
//            System.out.println(apiResponse.getData().get(0));
//
//            // Filtriranje samo potrebnih podataka
//            return apiResponse.getData().stream()
//                    .map(country -> {
//                        Country response = new Country();
//                        response.setCountryName(country.getCountryName());
//                        response.setCountryId(country.getCountryId());
//                        return response;
//                    })
//                    .collect(Collectors.toList());
//        }
//
//        return Collections.emptyList();
//    }
//
//    @Override
//    @Cacheable(value = "flightDataCache")
//    public List<City> getCities(String accessKey) {
//        // Pozivanje eksternog API-ja
//        String url = externalApiUrl + "/cities?access_key=" + accessKey;
//
//        ParameterizedTypeReference<ApiResponse<City>> responseType = new ParameterizedTypeReference<>() {};
//        ResponseEntity<ApiResponse<City>> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                null,
//                responseType
//        );
//
//        ApiResponse<City> apiResponse = responseEntity.getBody();
//
//        if (apiResponse != null && apiResponse.getData() != null) {
//            System.out.println(apiResponse.getData().get(0));
//
//            // Filtriranje samo potrebnih podataka
//            return apiResponse.getData().stream()
//                    .map(city -> {
//                        City response = new City();
//                        response.setId(city.getId());
//                        response.setName(city.getName());
//                        response.setIataCode(city.getIataCode());
//                        response.setIso2(city.getIso2());
//                        return response;
//                    })
//                    .collect(Collectors.toList());
//        }
//
//        return Collections.emptyList();
//    }
//

    @Override
    public List<City> getCities(String accessKey) {
        return null;
    }

    @Override
    public FlightInformationDto addFlight(FlightInformationDto flightInformationDto) {
        FlightInformation flight = mapToFlightInformation(flightInformationDto);
        FlightInformation savedFlight = flightRepository.save(flight);
        return mapToFlightInformationDto(savedFlight);
    }
    @Override
    public FlightInformationDto getFlightById(Long id) {
        Optional<FlightInformation> flightInformation= flightRepository.findById(id);
        if(!flightInformation.isPresent()){
            throw new NotFoundException("Flight with provided Id doesn't exist!", "404");
        }

        return mapToFlightInformationDto(flightInformation.get());
    }

    @Override
    public List<FlightInformationDto> getAllFlights() {
       List<FlightInformation> flightInformationDtoList = flightRepository.findAll();

       return mapToFlightInformationListDto(flightInformationDtoList);

    }

    @Override
    public FlightInformationDto updateFlight(Long id, FlightInformationDto flightInformationDto) {
        return null;
    }

    @Override
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }




}