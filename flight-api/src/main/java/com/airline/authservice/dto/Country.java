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
public class Country {
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("country_id")
    private String countryId;

    @JsonProperty("country_iso2")
    private String iso2;
}
