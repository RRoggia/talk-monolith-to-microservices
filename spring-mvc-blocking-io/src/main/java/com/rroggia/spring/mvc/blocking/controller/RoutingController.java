package com.rroggia.spring.mvc.blocking.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class RoutingController {

	private static final String ENGINE_URL = "http://localhost:8081/route";

	@GetMapping(path = "route", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> route(@RequestParam String delay) {

		String uri = UriComponentsBuilder.fromHttpUrl(ENGINE_URL).queryParam("delay", delay).toUriString();

		try {
			String response = new RestTemplate().getForObject(uri, String.class);
			return ResponseEntity.ok().body(response);
		} catch (HttpClientErrorException.BadRequest | HttpServerErrorException.InternalServerError e) {
			return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
		}
	}
}
