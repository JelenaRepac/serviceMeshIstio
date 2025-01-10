package com.airline.flightservice.security;

public final class SecurityConstants {

    public static final String SECRET = "mySecretKey";

    public static final long TOKEN_EXPIRATION_TIME = 86400000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_PATH = "/auth/login";
    public static final String REGISTRATION_PATH = "/auth/register";
    public static final String CONFIRMATION_PATH = "/auth/confirm-account";
}