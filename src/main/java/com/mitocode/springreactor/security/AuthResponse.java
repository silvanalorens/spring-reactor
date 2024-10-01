package com.mitocode.springreactor.security;

import java.util.Date;

//Clase S3
public record AuthResponse(
        String token,
        Date expiration
) {
}
