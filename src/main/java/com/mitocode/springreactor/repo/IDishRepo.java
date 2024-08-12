package com.mitocode.springreactor.repo;

import com.mitocode.springreactor.model.Dish;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IDishRepo extends ReactiveMongoRepository<Dish,String> {

}
