package com.rroggia.spring.mvc.non.blocking.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class RoutingController {

	private static WebClient webClient = WebClient.create("http://localhost:8081");

	@GetMapping(path = "route", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Mono<String> route(@RequestParam String delay) {

		return Mono.just(1).flatMap(i -> {
			return webClient.get().uri("/route?delay=" + delay)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
					.bodyToMono(String.class);
		});

	}
}
