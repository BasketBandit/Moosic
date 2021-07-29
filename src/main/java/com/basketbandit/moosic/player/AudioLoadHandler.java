package com.basketbandit.moosic.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            audioTrack.setUserData(toTime(audioTrack.getDuration()));
            audioTrackScheduler.queue(audioTrack);
        }, playlist -> playlist.getTracks().forEach(audioTrack -> {
            audioTrack.setUserData(toTime(audioTrack.getDuration()));
            audioTrackScheduler.queue(audioTrack);
        }), null, null));
    }

    public String toTime(long ms) {
        long second = (ms / 1000) % 60;
        long minute = (ms / (1000 * 60));
        return String.format("%02d:%02d", minute, second);
    }
}
