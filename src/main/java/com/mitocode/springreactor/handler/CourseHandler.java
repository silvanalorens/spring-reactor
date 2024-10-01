package com.mitocode.springreactor.handler;

import com.mitocode.springreactor.dto.CourseDTO;
import com.mitocode.springreactor.model.Course;
import com.mitocode.springreactor.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class CourseHandler {
    private final ICourseService service;
    private final ModelMapper modelMapper;
    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().map(this::convertToDto),CourseDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        String id = request.pathVariable("id");
        return service.findById(id)
                .map(this::convertToDto)
                .flatMap(e->ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request){
        Mono<CourseDTO> monoStudentDTO = request.bodyToMono(CourseDTO.class);

        return monoStudentDTO.flatMap(e->service.save(this.convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e-> ServerResponse
                                .created(URI.create(request.uri().toString().concat("/").concat(e.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(monoStudentDTO))
                );
    }

    public Mono<ServerResponse> update(ServerRequest request){
        String id = request.pathVariable("id");
        return request.bodyToMono(CourseDTO.class)
                .map(e-> {

                    e.setId(id);
                    return e;
                })
                .flatMap(e-> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String id = request.pathVariable("id");
        return service.delete(id)
                .flatMap(result -> {
                    if(result){
                        return ServerResponse.noContent().build();
                    }
                    else {
                        return ServerResponse.notFound().build();
                    }
                });
    }

    private CourseDTO convertToDto(Course model) {
        return modelMapper.map(model, CourseDTO.class);
    }

    private Course convertToDocument(CourseDTO dto){
        return modelMapper.map(dto, Course.class);
    }


}
