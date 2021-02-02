package com.ratelimiter.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ratelimiter.demo.exception.RateLimiterException;
import com.ratelimiter.demo.service.RateLimiterService;

@RestController
public class CommonController {

	@Autowired
	private RateLimiterService limiter;

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> request(String userlicense) throws Exception {

		Map<String, Object> status = new HashMap<String, Object>();
		if (limiter.isLimited(userlicense)) {
			throw new RateLimiterException("API rate limit exceeded for user " + userlicense);
		}
		System.out.println("Handle the request for user: " + userlicense);
		status.put("Status", "OK!");
		status.put("Message", userlicense);

		return status;
	}
}
