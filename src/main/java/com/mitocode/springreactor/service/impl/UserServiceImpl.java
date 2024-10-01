package com.mitocode.springreactor.service.impl;

import com.mitocode.springreactor.model.Role;
import com.mitocode.springreactor.model.User;
import com.mitocode.springreactor.repo.IGenericRepo;
import com.mitocode.springreactor.repo.IRoleRepo;
import com.mitocode.springreactor.repo.IUserRepo;
import com.mitocode.springreactor.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CRUDImpl<User, String> implements IUserService {
    private final IUserRepo userRepo;
    private final IRoleRepo roleRepo;
    private final BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepo<User, String> getRepo() {
        return userRepo;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Mono<com.mitocode.springreactor.security.User> searchByUser(String username) {
        return userRepo.findOneByUsername(username)
                .flatMap(user-> Flux.fromIterable(user.getRoles())
                .flatMap(userRole ->roleRepo.findById(userRole.getId())
                        .map(Role::getName))
                        .collectList()
                        .map(roles-> new com.mitocode.springreactor.security.User(user.getUsername(),user.getPassword(),user.isStatus(),roles) )
                );
    }
}
