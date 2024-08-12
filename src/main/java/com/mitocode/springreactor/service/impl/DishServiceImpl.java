package com.mitocode.springreactor.service.impl;

import com.mitocode.springreactor.model.Dish;
import com.mitocode.springreactor.repo.IDishRepo;
import com.mitocode.springreactor.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements IDishService {
    private final IDishRepo repo;


    @Override
    public Mono<Dish> save(Dish dish) {
        return repo.save(dish);
    }

    @Override
    public Mono<Dish> update(String id, Dish dish) {
        dish.setId(id);
        return repo.save(dish);
    }

    @Override
    public Flux<Dish> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<Dish> findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return repo.deleteById(id).thenReturn(true);
    }
}
