package com.mitocode.springreactor.controller;

import com.mitocode.springreactor.model.Tuition;
import com.mitocode.springreactor.service.ITuitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
//Controller y ResponseBody devuelve json
@RequestMapping("/tuitions")
@RequiredArgsConstructor
public class TuitionController {
    private final ITuitionService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Tuition>>>  findAll(){
        Flux<Tuition> fx = service.findAll();
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Tuition>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(e->ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<Tuition>> save(@Valid @RequestBody Tuition tuition,
                                             final ServerHttpRequest req){
        return service.save(tuition)
                .map(e -> ResponseEntity.created(
                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Tuition>> update(@Valid @PathVariable("id") String id, @RequestBody Tuition tuition){
        return Mono.just(tuition)
                .map(e->{
                    e.setId(id);
                    return e;
                })
                .flatMap(e->service.update(id,e))
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());

    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return service.delete(id)
                .flatMap(result->{
                        if (result)
                            return Mono.just(ResponseEntity.noContent().build());
                        else
                            return Mono.just(ResponseEntity.notFound().build());
                });
    }
    @GetMapping("/generateReport/{id}")
    public Mono<ResponseEntity<byte[]>> generateReport(@PathVariable("id") String id){
        return service.generateReport(id)
                .map(bytes ->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(bytes))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
