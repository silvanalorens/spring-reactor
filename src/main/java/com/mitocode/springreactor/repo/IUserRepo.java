package com.mitocode.springreactor.repo;

import com.mitocode.springreactor.model.User;

import reactor.core.publisher.Mono;

public interface IUserRepo extends IGenericRepo<User,String> {

    Mono<User> findOneByUsername(String username);
}
