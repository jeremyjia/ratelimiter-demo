package com.ratelimiter.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class HelloApplication {

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("homepage.html");
		return mv;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HelloApplication.class, args);
		System.out.println("I am ready!");

	}

}