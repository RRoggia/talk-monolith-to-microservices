package com.rroggia.spring.mvc.blocking.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutingController {

	@PostMapping(path = "route", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String route(@RequestParam String delay) {
		return "{\"it\":\"worked\"}";
	}

}
