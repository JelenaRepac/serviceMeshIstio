package com.airline.authservice.security;

public final class
SecurityConstants {

    public static final String SECRET = "9cD3O9y5bWyvwrfMno9Lkij/9MfX6D9jFz3kQ8DTRzHfwtD9f1ds29bgmJsdUgCT";

    public static final long TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24;

    public static final long REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_PATH = "/api/login";
    public static final String REGISTRATION_PATH = "/api/auth/register";
    public static final String CONFIRMATION_PATH = "/api/auth/confirm-account";

    public static final String REFRESH_TOKEN =    "api/auth/token";


    public static final String JWKS_URI_PATH="/auth/.well-known/jwks.json";
}