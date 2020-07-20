package com.iavtar.authservice.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

	@GetMapping("/")
	@PreAuthorize("hasRole('USER')")
	public String echo(Principal principal) {
		return principal.getName();
	}

}
