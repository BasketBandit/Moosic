package com.basketbandit.moosic;

import com.basketbandit.moosic.player.AudioTrackScheduler;
import com.basketbandit.moosic.player.LavaPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
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
	private final LavaPlayer lavaPlayer = new LavaPlayer();
	private final AudioPlayer player = lavaPlayer.getPlayer();
	private final AudioTrackScheduler scheduler = lavaPlayer.getAudioTrackScheduler();

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
	public ModelAndView dashboard() {
		ModelAndView modelAndView = new ModelAndView("dashboard");
		modelAndView.addObject("player", player);
		modelAndView.addObject("queue", scheduler.getQueue());
		modelAndView.addObject("history", scheduler.getHistory());
		modelAndView.addObject("active", scheduler.getActiveTrack());
		modelAndView.addObject("progress", scheduler.getActiveTrackProgress());
		return modelAndView;
	}

	@PostMapping("/action")
	public void processAction(@RequestParam(value = "parameter", required = false) String parameter, @RequestParam(value = "value", required = false) String value) {
		if(parameter != null) {
			switch(parameter) {
				case "play" -> player.setPaused(!player.isPaused());
				case "skip" -> {
					if(scheduler.getActiveTrack() != null) {
						scheduler.onTrackEnd(player, scheduler.getActiveTrack(), AudioTrackEndReason.FINISHED);
					}
				}
				case "remove" -> {
					if(value != null) {
						scheduler.remove(Integer.parseInt(value));
					}
				}
				case "shuffle" -> scheduler.shuffle();
				case "clearQueue" -> scheduler.getQueue().clear();
				case "clearHistory" -> scheduler.getHistory().clear();
				case "volume" -> {
					if(value != null) {
						player.setVolume(Math.min(100, Math.max(0, Integer.parseInt(value))));
					}
				}
			}
		}
	}

	@GetMapping("/queue")
	public ModelAndView getQueue() {
		ModelAndView modelAndView = new ModelAndView("queue");
		modelAndView.addObject("queue", scheduler.getQueue());
		return modelAndView;
	}

	@GetMapping("/history")
	public ModelAndView getHistory() {
		ModelAndView modelAndView = new ModelAndView("history");
		modelAndView.addObject("history", scheduler.getHistory());
		return modelAndView;
	}

	@GetMapping("/active")
	public ModelAndView getActive() {
		ModelAndView modelAndView = new ModelAndView("active");
		modelAndView.addObject("active", scheduler.getActiveTrack());
		return modelAndView;
	}

	@GetMapping("/progress")
	public ModelAndView getProgress() {
		ModelAndView modelAndView = new ModelAndView("progress");
		modelAndView.addObject("active", scheduler.getActiveTrack());
		modelAndView.addObject("progress", scheduler.getActiveTrackProgress());
		return modelAndView;
	}

	@GetMapping("/controls")
	public ModelAndView getControls() {
		ModelAndView modelAndView = new ModelAndView("controls");
		modelAndView.addObject("player", player);
		return modelAndView;
	}
}
