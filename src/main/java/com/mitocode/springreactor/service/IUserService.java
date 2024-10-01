package com.mitocode.springreactor.service;

import com.mitocode.springreactor.model.User;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<User> saveHash(User user);
    Mono<com.mitocode.springreactor.security.User> searchByUser(String username);
}
