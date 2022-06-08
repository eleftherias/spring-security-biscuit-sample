package com.example.biscuitsample.web;

import com.clevercloud.biscuit.crypto.KeyPair;
import com.clevercloud.biscuit.error.Error;
import com.clevercloud.biscuit.token.Biscuit;

import java.time.Duration;
import java.time.Instant;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
	private final KeyPair root;

	public TokenController(KeyPair root) {
		this.root = root;
	}

	@PostMapping("/token")
	public String token(Authentication authentication) throws Error {
		String usernameFact = "user(\"" + authentication.getName() + "\")";
		Instant expiresAt = Instant.now().plus(Duration.ofHours(2)); // token expires after 2 hours
		return Biscuit.builder(root)
				.add_authority_fact(usernameFact)
				.add_authority_check("check if time($date), $date <= " + expiresAt.toString())
				.build()
				.serialize_b64url();
	}
}
