package com.mitocode.springreactor.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-1)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

    /**
     * Create a new {@code AbstractErrorWebExceptionHandler}.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param applicationContext the application context
     * @since 2.4.0
     */
    public WebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse);
    }

    private  Mono<ServerResponse> renderErrorResponse(ServerRequest req) {
        Map<String, Object> generalError=getErrorAttributes(req, ErrorAttributeOptions.defaults());
        Map<String, Object> customError= new HashMap<>();
        int statusCode = Integer.parseInt(String.valueOf(generalError.get("status")));
        Throwable error = getError(req);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (statusCode){
            case 400,402 ->{
                customError.put("message", error.getMessage());
                customError.put("status", 400);
                httpStatus = HttpStatus.BAD_REQUEST;
            }
            default -> {
                customError.put("message", "no personalizado");
                customError.put("status",500);
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customError));

    }
}
