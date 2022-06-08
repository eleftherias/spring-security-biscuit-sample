package com.example.biscuitsample.biscuit;

import com.clevercloud.biscuit.error.Error;
import com.clevercloud.biscuit.token.Biscuit;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class BiscuitAuthenticationToken extends AbstractAuthenticationToken {
	private final String name;
	private final Biscuit biscuit;

	public BiscuitAuthenticationToken(Biscuit biscuit) {
		super(List.of(new SimpleGrantedAuthority("ROLE_USER"))); // alternatively get roles from biscuit
		this.name = getUsername(biscuit);
		this.biscuit = biscuit;
		this.setAuthenticated(true);
	}

	@Override
	public Biscuit getCredentials() {
		return this.biscuit;
	}

	@Override
	public Object getPrincipal() {
		return this.biscuit;
	}

	@Override
	public String getName() {
		return this.name;
	}

	private String getUsername(Biscuit biscuit) {
		String name;
		try {
			name = biscuit
					.authorizer()
					.query("data($name) <- user($name)")
					.stream()
					.findFirst()
					.orElseThrow()
					.terms()
					.stream()
					.findFirst()
					.orElseThrow()
					.toString();
		} catch (Error.Parser | Error.FailedLogic | Error.Timeout | Error.TooManyIterations | Error.TooManyFacts e) {
			name = null;
		}
		return name;
	}
}
