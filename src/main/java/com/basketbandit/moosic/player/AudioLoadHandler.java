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
        manager.loadItem(url, new FunctionalResultHandler(audioTrackScheduler::queue, playlist -> playlist.getTracks().forEach(audioTrackScheduler::queue), null, null));
    }
}
