package com.airline.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class City  {

    private String id;
    @JsonProperty("city_name")
    private String name;
    @JsonProperty("iata_code")
    private String iataCode;
    @JsonProperty("country_iso2")
    private String iso2;
}
