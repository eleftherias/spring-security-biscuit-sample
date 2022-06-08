package com.example.biscuitsample.biscuit;

import com.clevercloud.biscuit.crypto.KeyPair;
import com.clevercloud.biscuit.error.Error;
import com.clevercloud.biscuit.token.Biscuit;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Instant;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

public class BiscuitAuthenticationProvider implements AuthenticationProvider {
	private final KeyPair root;

	public BiscuitAuthenticationProvider(KeyPair root) {
		this.root = root;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		BearerTokenAuthenticationToken bearer = (BearerTokenAuthenticationToken) authentication;
		Biscuit verifiedBiscuit = getVerifiedBiscuit(bearer);
		return new BiscuitAuthenticationToken(verifiedBiscuit);
	}

	private Biscuit getVerifiedBiscuit(BearerTokenAuthenticationToken bearer) {
		try {
			Biscuit biscuit = Biscuit.from_b64url(bearer.getToken(), this.root.public_key());
			String currentTime = Instant.now().toString();
			biscuit.authorizer()
					.add_fact("time(" + currentTime + ")")
					.allow()
					.authorize();
			return biscuit;
		} catch (NoSuchAlgorithmException | SignatureException | Error failed) {
			throw new InvalidBearerTokenException(failed.getMessage(), failed);
		} catch (InvalidKeyException failed) {
			throw new AuthenticationServiceException(failed.getMessage(), failed);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
