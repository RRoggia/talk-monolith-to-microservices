package com.rroggia.spring.mvc.blocking.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

	@GetMapping(path = "route", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> route(@RequestParam String delay) {

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/route")
				.queryParam("delay", delay);

		ResponseEntity<String> exchange;
		try {
			exchange = new RestTemplate().exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, String.class);
		} catch (HttpClientErrorException.BadRequest | HttpServerErrorException.InternalServerError e) {
			return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(exchange.getBody(), HttpStatus.OK);
	}

}
