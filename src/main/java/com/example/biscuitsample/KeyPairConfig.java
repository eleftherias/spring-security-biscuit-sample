package com.example.biscuitsample;

import com.clevercloud.biscuit.crypto.KeyPair;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyPairConfig {

	@Bean
	public KeyPair root() {
		return new KeyPair();
	}

}
