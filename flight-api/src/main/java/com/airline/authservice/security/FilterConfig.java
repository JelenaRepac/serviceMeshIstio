package com.airline.authservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Value("${secure.key.token}")
    private String secretKey;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(secretKey);
    }
}
