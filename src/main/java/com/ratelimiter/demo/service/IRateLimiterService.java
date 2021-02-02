package com.ratelimiter.demo.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface IRateLimiterService<T> {

	boolean isLimited(T license);

}
