package com.rroggia.spring.webflux.non.blocking.controller;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RoutingHandler {

	private static WebClient webClient = WebClient.create("http://localhost:8081");

	@Bean
	public RouterFunction<?> routes() {
		return RouterFunctions.route().GET("/route", request -> {
			Optional<String> delay = request.queryParam("delay");

			return webClient.get().uri("/route?delay=" + delay.get())
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
					.bodyToMono(String.class).flatMap(body -> ServerResponse.ok().syncBody(body));

		}).build();
	}

}
