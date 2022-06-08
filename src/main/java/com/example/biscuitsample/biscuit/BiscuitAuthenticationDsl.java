package com.example.biscuitsample.biscuit;

import com.clevercloud.biscuit.crypto.KeyPair;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

public class BiscuitAuthenticationDsl extends AbstractHttpConfigurer<BiscuitAuthenticationDsl, HttpSecurity> {

	private KeyPair root;

	@Override
	public void configure(HttpSecurity http) {
		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		BearerTokenAuthenticationFilter filter = new BearerTokenAuthenticationFilter(authenticationManager);
		filter.setBearerTokenResolver(new DefaultBearerTokenResolver());
		filter.setAuthenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
		http.addFilter(filter);
		http.authenticationProvider(new BiscuitAuthenticationProvider(this.root));
	}

	public BiscuitAuthenticationDsl keyPair(KeyPair root) {
		this.root = root;
		return this;
	}

	public static BiscuitAuthenticationDsl biscuitAuthenticationDsl() {
		return new BiscuitAuthenticationDsl();
	}
}
