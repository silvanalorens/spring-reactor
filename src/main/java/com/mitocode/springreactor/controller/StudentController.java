package com.mitocode.springreactor.controller;

import com.mitocode.springreactor.dto.StudentDTO;
import com.mitocode.springreactor.model.Course;
import com.mitocode.springreactor.model.Student;
import com.mitocode.springreactor.service.ICourseService;
import com.mitocode.springreactor.service.IStudentService;
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

@RestController
//Controller y ResponseBody devuelve json
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final IStudentService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<StudentDTO>>>  findAll(){
        Flux<StudentDTO> fx = service.findAll().map(this::convertToDto);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<StudentDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertToDto)
                .map(e->ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<StudentDTO>> save(@Valid @RequestBody StudentDTO studentDTO,
                                             final ServerHttpRequest req){
        return service.save(this.convertToDocument(studentDTO))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(
                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<StudentDTO>> update(@PathVariable("id") String id, @RequestBody StudentDTO studentDTO){
        return Mono.just(convertToDocument(studentDTO))
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
    private StudentDTO convertToDto(Student model) {
        return modelMapper.map(model, StudentDTO.class);
    }

    private Student convertToDocument(StudentDTO dto) {
        return modelMapper.map(dto, Student.class);
    }
}
