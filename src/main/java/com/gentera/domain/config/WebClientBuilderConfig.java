package com.gentera.domain.config;

import static java.util.Objects.requireNonNull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.logbook.CorrelationId;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.autoconfigure.webflux.LogbookWebFluxAutoConfiguration;
import org.zalando.logbook.netty.LogbookClientHandler;

import brave.Tracer;
import io.micrometer.observation.ObservationRegistry;
import reactor.netty.http.client.HttpClient;

@Configuration
@Import(LogbookWebFluxAutoConfiguration.class)
public class WebClientBuilderConfig {
	
	@Order(-1)
    @Bean(name = "CustomWebClientBuilder", autowireCandidate = true)
    WebClient.Builder getWebClientBuilderBean(final Logbook logbook) {
    	HttpClient httpClient = HttpClient
    			.create()
    			.doOnConnected(connection -> connection.addHandlerLast(new LogbookClientHandler(logbook)));
    	
		return WebClient
				.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.observationRegistry(ObservationRegistry.create());
	}

    @Bean
    CorrelationId correlationId(final Tracer tracer) {
		return request -> requireNonNull(requireNonNull(tracer.currentSpan())).context().traceIdString();
	}
    
}
