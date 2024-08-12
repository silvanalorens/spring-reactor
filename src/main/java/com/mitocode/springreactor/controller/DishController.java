package com.mitocode.springreactor.controller;

import com.mitocode.springreactor.model.Dish;
import com.mitocode.springreactor.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
//Controller y ResponseBody devuelve json
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {
    private final IDishService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Dish>>>  findAll(){
        Flux<Dish> fx = service.findAll();
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Dish>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(e->ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<Dish>> save(@RequestBody Dish dish){
        return service.save(dish)
                .map(mx -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mx));
    }
    @PutMapping("/{id}")
    public Mono<Dish> update(@PathVariable("id") String id,@RequestBody Dish dish){
        return service.update(id,dish);
    }
    @DeleteMapping("/{id}")
    public Mono<Boolean> delete(@PathVariable("id") String id){
        return service.delete(id);
    }

}
