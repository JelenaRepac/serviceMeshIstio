package com.airline.flightservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.airline.flightservice.security.SecurityConstants.SECRET;
import java.util.Base64;

@RestController
@RequestMapping("/.well-known")
public class JWKSController {

    @GetMapping("/jwks.json")
    public String getJWKS() {
        // Your secret

        // Base64 encode the secret (the key is usually base64 encoded)
        String base64Secret = Base64.getEncoder().encodeToString(SECRET.getBytes());

        // Return the JWKS response
        return "{\n" +
                "  \"keys\": [\n" +
                "    {\n" +
                "      \"kty\": \"oct\",\n" +  // 'oct' means symmetric key (HMAC)
                "      \"kid\": \"1\",\n" +    // Key ID, typically a unique identifier for the key
                "      \"use\": \"sig\",\n" +  // The key is used for signing
                "      \"k\": \"" + base64Secret + "\"\n" +  // The base64-encoded secret
                "    }\n" +
                "  ]\n" +
                "}";
    }

}
