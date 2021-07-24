package com.basketbandit.moosic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@RestController
public class MoosicApplication {
	public static void main(String[] args) {
		SpringApplication.run(MoosicApplication.class, args);
	}

	@GetMapping("/")
	public ModelAndView root(@RequestParam(value = "name", defaultValue = "World") String name) {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("name", name);
		return modelAndView;
	}
}
