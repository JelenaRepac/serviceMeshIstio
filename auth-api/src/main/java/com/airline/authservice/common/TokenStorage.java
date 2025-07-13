package com.airline.authservice.common;

import com.google.api.client.auth.oauth2.Credential;

import java.util.HashMap;
import java.util.Map;

public class TokenStorage {
    private static final Map<Long, Credential> tokenMap = new HashMap<>();

    public static void save(Long userId, Credential credential) {
        tokenMap.put(userId, credential);
    }

    public static Credential get(Long userId) {
        return tokenMap.get(userId);
    }
}
