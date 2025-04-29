package com.airline.authservice.security;

public final class SecurityConstants {

    public static final String SECRET = "9cD3O9y5bWyvwrfMno9Lkij/9MfX6D9jFz3kQ8DTRzHfwtD9f1ds29bgmJsdUgCT";

    public static final long TOKEN_EXPIRATION_TIME = 86400000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_PATH = "/login";
    public static final String REGISTRATION_PATH = "/auth/register";
    public static final String CONFIRMATION_PATH = "/auth/confirm-account";

    public static final String JWKS_URI_PATH="/auth/.well-known/jwks.json";
}