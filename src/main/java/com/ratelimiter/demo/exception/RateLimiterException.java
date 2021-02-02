package com.ratelimiter.demo.exception;

public class RateLimiterException extends Exception{

	private static final long serialVersionUID = -8339191474259805292L;

	public RateLimiterException() {
		super();
	}

	public RateLimiterException(String message) {
		super(message);
	}

}
