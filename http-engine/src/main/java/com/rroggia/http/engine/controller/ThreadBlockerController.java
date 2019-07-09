package com.rroggia.http.engine.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreadBlockerController {
	private static final int MILLISECONDS = 1000;

	@PostMapping(value = "route", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String route(@RequestParam String delay) {
		try {
			Thread.sleep(Long.valueOf(delay) * MILLISECONDS);
		} catch (NumberFormatException | InterruptedException e) {
			e.printStackTrace();
			return "{\"error\":\"didn't finished sleep\"}";
		}
		return "{\"worked\":\"processed after " + delay + "\"}";
	}
}
