package com.airline.authservice.dto;

import com.airline.authservice.model.Country;
import com.airline.authservice.model.Pagination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private Pagination pagination;
    private List<Country> data;

}
