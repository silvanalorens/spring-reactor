package com.mitocode.springreactor.service;

import com.mitocode.springreactor.model.Tuition;
import reactor.core.publisher.Mono;

public interface ITuitionService extends ICRUD<Tuition,String> {
    Mono<byte[]> generateReport(String idTuition);

}
