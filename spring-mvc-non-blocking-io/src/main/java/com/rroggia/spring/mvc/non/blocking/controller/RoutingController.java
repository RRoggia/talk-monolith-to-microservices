package com.rroggia.spring.mvc.non.blocking.controller;

import java.util.concurrent.ForkJoinPool;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@RestController
public class RoutingController {

	private static final String ENGINE_URL = "http://localhost:8081";
	private static final String ENGINE_WITH_ROUTE_ENDPOINT = "http://localhost:8081/route";
	private static WebClient webClient = WebClient.create(ENGINE_URL);

	@GetMapping(path = "route", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Mono<String> route(@RequestParam String delay) {

		return webClient.get().uri("/route?delay=" + delay)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
				.onStatus(HttpStatus::is4xxClientError, e -> Mono.error(new RuntimeException("e")))
				.onStatus(HttpStatus::is5xxServerError, e -> Mono.error(new RuntimeException("e")))
				.bodyToMono(String.class);

	}

	@GetMapping(path = "anotherRoute", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public DeferredResult<ResponseEntity<String>> anotherRoute(@RequestParam String delay) {
		DeferredResult<ResponseEntity<String>> response = new DeferredResult<>();

		ForkJoinPool.commonPool().submit(() -> {
			String uri = UriComponentsBuilder.fromHttpUrl(ENGINE_WITH_ROUTE_ENDPOINT).queryParam("delay", delay).toUriString();

			try {
				response.setResult(ResponseEntity.ok().body(new RestTemplate().getForObject(uri, String.class)));
			} catch (HttpClientErrorException.BadRequest | HttpServerErrorException.InternalServerError e) {
				response.setResult(ResponseEntity.badRequest().body(e.getResponseBodyAsString()));
			}

		});

		return response;
	}
}
