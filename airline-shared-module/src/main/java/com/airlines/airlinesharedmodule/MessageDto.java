package com.airlines.airlinesharedmodule;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private String code;
    private String message;
}
