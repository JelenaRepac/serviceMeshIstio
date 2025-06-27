package com.airline.authservice.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

//Added comment
@Component
public class ConvertToJson {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        //Dodato zbog toga sto Jackson ne podrzava date/time iz Jave8
        objectMapper.registerModule(new JavaTimeModule());
        //Da bi datum bio citljiv, a ne u formatu timestamp-a
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static String convertObjectToJsonString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException("Request body nije odgovarajućeg formata!");
        }
    }


}
