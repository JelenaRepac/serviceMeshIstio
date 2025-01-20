package com.airline.flightservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("country_id")
    private String countryId;
}
