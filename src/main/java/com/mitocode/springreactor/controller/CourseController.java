package com.mitocode.springreactor.controller;

import com.mitocode.springreactor.dto.CourseDTO;
import com.mitocode.springreactor.model.Course;
import com.mitocode.springreactor.service.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@RestController
//Controller y ResponseBody devuelve json
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final ICourseService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<CourseDTO>>>  findAll(){
        Flux<CourseDTO> fx = service.findAll().map(this::convertToDTO);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CourseDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertToDTO)
                .map(e->ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<CourseDTO>> save(@Valid @RequestBody CourseDTO courseDTO,
                                             final ServerHttpRequest req){
        return service.save(this.convertToDocument(courseDTO))
                .map(this::convertToDTO)
                .map(e -> ResponseEntity.created(
                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CourseDTO>> update(@Valid @PathVariable("id") String id, @RequestBody CourseDTO dto){
        return Mono.just(this.convertToDocument(dto))
                .map(e->{
                    e.setId(id);
                    return e;
                })
                .flatMap(e->service.update(id,e))
                .map(this::convertToDTO)
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
    private CourseDTO convertToDTO(Course model) {
        return modelMapper.map(model, CourseDTO.class);
    }
    private Course convertToDocument(CourseDTO dto) {
        return modelMapper.map(dto, Course.class);
    }
}
