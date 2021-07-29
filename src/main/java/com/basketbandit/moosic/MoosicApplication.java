package com.basketbandit.moosic;

import com.basketbandit.moosic.player.LavaPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
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
	private LavaPlayer lavaPlayer = new LavaPlayer();

	public static void main(String[] args) {
		SpringApplication.run(MoosicApplication.class, args);
	}

	@GetMapping("/")
	public ModelAndView root() {
		return new ModelAndView("index");
	}

	@PostMapping("/load")
	public RedirectView load(@RequestParam(value = "url") String url) {
		if(url != null) {
			lavaPlayer.getAudioLoadHandler().load(url);
		}
		return new RedirectView("/");
	}

	@GetMapping("/dashboard")
	public ModelAndView dashboard(@RequestParam(value = "action", required = false) String action) {
		if(action != null){
			switch(action) {
				case "skip" -> {
					if(lavaPlayer.getPlayer().getPlayingTrack() != null) {
						lavaPlayer.getAudioTrackScheduler().onTrackEnd(lavaPlayer.getPlayer(), lavaPlayer.getPlayer().getPlayingTrack(), AudioTrackEndReason.FINISHED);
					}
				}
				case "pause" -> lavaPlayer.getPlayer().setPaused(!lavaPlayer.getPlayer().isPaused());
			}
		}

		// despite code duplication with getData(), it stops flickering when refreshing the page.
		ModelAndView modelAndView = new ModelAndView("dashboard");
		modelAndView.addObject("queue", lavaPlayer.getAudioTrackScheduler().getQueue());
		modelAndView.addObject("history", lavaPlayer.getAudioTrackScheduler().getHistory());
		modelAndView.addObject("current", lavaPlayer.getPlayer().getPlayingTrack());
		return modelAndView;
	}

	@GetMapping("/queue")
	public ModelAndView getQueue() {
		ModelAndView modelAndView = new ModelAndView("queue");
		modelAndView.addObject("queue", lavaPlayer.getAudioTrackScheduler().getQueue());
		return modelAndView;
	}

	@GetMapping("/history")
	public ModelAndView getHistory() {
		ModelAndView modelAndView = new ModelAndView("history");
		modelAndView.addObject("history", lavaPlayer.getAudioTrackScheduler().getHistory());
		return modelAndView;
	}

	@GetMapping("/status")
	public ModelAndView getStatus() {
		ModelAndView modelAndView = new ModelAndView("status");
		modelAndView.addObject("current", lavaPlayer.getPlayer().getPlayingTrack());
		return modelAndView;
	}
}
