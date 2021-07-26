package com.basketbandit.moosic;

import com.basketbandit.moosic.player.LavaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
@RestController
public class MoosicApplication {
	private static final Logger log = LoggerFactory.getLogger(MoosicApplication.class);
	private LavaPlayer player = new LavaPlayer();

	public static void main(String[] args) {
		SpringApplication.run(MoosicApplication.class, args);
	}

	@GetMapping("/")
	public ModelAndView root() {
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}

	@PostMapping("/load")
	public RedirectView load(@RequestParam(value = "url") String url) {
		if(url != null) {
			player.getAudioLoadHandler().load(url);
		}
		RedirectView redirectView = new RedirectView("/");
		return redirectView;
	}

	@GetMapping("/dashboard")
	public ModelAndView dashboard() {
		ModelAndView modelAndView = new ModelAndView("dashboard");
		return modelAndView;
	}
}
