package com.basketbandit.moosic.rest;

import com.basketbandit.moosic.Moosic;
import com.basketbandit.moosic.player.AudioTrackScheduler;
import com.basketbandit.moosic.player.LavaPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RestfulController {
    private static final Logger log = LoggerFactory.getLogger(Moosic.class);
    private final LavaPlayer lavaPlayer = Moosic.lavaPlayer;
    private final AudioPlayer player = lavaPlayer.getPlayer();
    private final AudioTrackScheduler scheduler = lavaPlayer.getAudioTrackScheduler();

    @GetMapping("/")
    public ModelAndView root() {
        return new ModelAndView("index");
    }

    @PostMapping("/load")
    public RedirectView load(@RequestParam(value = "url") String url) {
        if(url != null) {
            lavaPlayer.load(url);
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
