package com.mitocode.springreactor.controller;

import com.mitocode.springreactor.dto.TuitionDTO;
import com.mitocode.springreactor.model.Tuition;
import com.mitocode.springreactor.service.ITuitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("tuitionMapper")
    private final ModelMapper modelMapper;
    @GetMapping
    public Mono<ResponseEntity<Flux<TuitionDTO>>>  findAll(){
        Flux<TuitionDTO> fx = service.findAll().map(this::convertToDto);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TuitionDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertToDto)
                .map(e->ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<TuitionDTO>> save(@Valid @RequestBody TuitionDTO tuitionDTO,
                                             final ServerHttpRequest req){
        return service.save(convertToDocument(tuitionDTO))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(
                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TuitionDTO>> update(@Valid @PathVariable("id") String id, @RequestBody TuitionDTO tuitionDTO){
        return Mono.just(convertToDocument(tuitionDTO))
                .map(e->{
                    e.setId(id);
                    return e;
                })
                .flatMap(e->service.update(id,e))
                .map(this::convertToDto)
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
    private TuitionDTO convertToDto(Tuition model) {
        return modelMapper.map(model, TuitionDTO.class);
    }

    private Tuition convertToDocument(TuitionDTO dto) {
        return modelMapper.map(dto, Tuition.class);
    }
}
