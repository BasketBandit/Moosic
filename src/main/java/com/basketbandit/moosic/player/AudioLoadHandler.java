package com.basketbandit.moosic.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class AudioLoadHandler {
    private static final Logger log = LoggerFactory.getLogger(AudioLoadHandler.class);
    private final AudioPlayerManager manager;
    private final AudioTrackScheduler audioTrackScheduler;

    public AudioLoadHandler(AudioPlayerManager manager, AudioTrackScheduler audioTrackScheduler) {
        this.manager = manager;
        this.audioTrackScheduler = audioTrackScheduler;
    }

    public void load(String url) {
        manager.loadItem(url, new FunctionalResultHandler(audioTrack -> {
            audioTrack.setUserData(new HashMap<>() {{
                put("position", toTime(0));
                put("duration", toTime(audioTrack.getDuration()));
            }});
            audioTrackScheduler.queue(audioTrack);
        }, playlist -> {
            playlist.getTracks().forEach(audioTrack -> {
                audioTrack.setUserData(new HashMap<>() {{
                    put("position", toTime(0));
                    put("duration", toTime(audioTrack.getDuration()));
                }});
            });
            audioTrackScheduler.queueAll(playlist.getTracks());
        }, null, null));
    }

    public static String toTime(long ms) {
        long second = (ms / 1000) % 60;
        long minute = (ms / (1000 * 60));
        return String.format("%02d:%02d", minute, second);
    }
}
