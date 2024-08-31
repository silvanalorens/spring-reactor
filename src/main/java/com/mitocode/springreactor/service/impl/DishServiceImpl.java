package com.mitocode.springreactor.service.impl;

import com.mitocode.springreactor.model.Dish;
import com.mitocode.springreactor.repo.IDishRepo;
import com.mitocode.springreactor.repo.IGenericRepo;
import com.mitocode.springreactor.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishServiceImpl extends CRUDImpl<Dish,String> implements IDishService {
    private final IDishRepo repo;

    @Override
    protected IGenericRepo<Dish, String> getRepo() {
        return repo;
    }


}
