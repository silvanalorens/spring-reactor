package com.mitocode.springreactor.controller;

import com.mitocode.springreactor.model.Student;
import com.mitocode.springreactor.security.AuthRequest;
import com.mitocode.springreactor.security.AuthResponse;
import com.mitocode.springreactor.security.JwtUtil;
import com.mitocode.springreactor.service.IStudentService;
import com.mitocode.springreactor.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

@RestController

@RequiredArgsConstructor
public class LoginController {
    private final JwtUtil jwtUtil;
    private final IUserService service;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest authRequest){
        return service.searchByUser(authRequest.getUsername())
                .map(userDetails -> {
                    if(BCrypt.checkpw(authRequest.getPassword(), userDetails.getPassword())){
                        String token = jwtUtil.generateToken(userDetails);
                        Date expiration = jwtUtil.getExpirationDateFromToken(token);

                        return ResponseEntity.ok(new AuthResponse(token, expiration));
                    }else{
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}
