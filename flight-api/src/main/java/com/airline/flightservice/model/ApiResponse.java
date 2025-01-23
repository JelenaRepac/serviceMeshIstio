package com.airline.flightservice.model;

import com.airline.flightservice.dto.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private Pagination pagination;
    private List<T> data;

}
