package com.airline.authapi.security;

import com.airline.authapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static  com.airline.authapi.security.SecurityConstants.*;
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private CustomAuthenticationProvider customAuthenticationProvider;
    private BCryptPasswordEncoder encoder;
//    private AdminRepository adminRepository;

    @Autowired
    public WebSecurity(CustomAuthenticationProvider customAuthenticationProvider,
                       BCryptPasswordEncoder encoder
                       // ,AdminRepository adminRepository
    ) {
        super();
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.encoder = encoder;
        //   this.adminRepository = adminRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().
                authorizeRequests().
                antMatchers(LOGIN_PATH, REGISTRATION_PATH, CONFIRMATION_PATH).permitAll()
                .anyRequest().authenticated().and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
            //    .addFilter(new JWTAuthorizationFilter(authenticationManager(), userRepo, adminRepository))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        customAuthenticationProvider.setEncoder(encoder);
        auth.authenticationProvider(customAuthenticationProvider);
    }
}