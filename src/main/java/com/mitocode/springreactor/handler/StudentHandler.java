package com.mitocode.springreactor.handler;

import com.mitocode.springreactor.dto.StudentDTO;
import com.mitocode.springreactor.model.Student;
import com.mitocode.springreactor.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class StudentHandler {
    private final IStudentService service;
    private final ModelMapper modelMapper;
    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().map(this::convertToDto),StudentDTO.class);
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
        Mono<StudentDTO> monoStudentDTO = request.bodyToMono(StudentDTO.class);

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
        return request.bodyToMono(StudentDTO.class)
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

    private StudentDTO convertToDto(Student model) {
        return modelMapper.map(model, StudentDTO.class);
    }

    private Student convertToDocument(StudentDTO dto){
        return modelMapper.map(dto, Student.class);
    }


}
