package com.gentera.domain.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class InfoHandler implements Handler<Mono<String>> {
    
    @Override
    public Mono<String> handle() {
    	return Mono.just("Hello World!");
    }
}
