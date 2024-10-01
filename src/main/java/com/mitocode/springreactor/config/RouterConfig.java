package com.mitocode.springreactor.config;

import com.mitocode.springreactor.handler.CourseHandler;
import com.mitocode.springreactor.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> routers1(StudentHandler handler){
        return route(GET("/v2/students"), handler::findAll)
                .andRoute(GET("/v2/students/{id}"), handler::findById)
                .andRoute(POST("/v2/students"), handler::save)
                .andRoute(PUT("/v2/students/{id}"), handler::update)
                .andRoute(DELETE("/v2/students/{id}"), handler::delete);
  }

    @Bean(name = "courseRoutes")
    public RouterFunction<ServerResponse> routers2(CourseHandler handler){
        return route(GET("/v2/courses"), handler::findAll)
                .andRoute(GET("/v2/courses/{id}"), handler::findById)
                .andRoute(POST("/v2/courses"), handler::save)
                .andRoute(PUT("/v2/courses/{id}"), handler::update)
                .andRoute(DELETE("/v2/courses/{id}"), handler::delete);
    }
}
