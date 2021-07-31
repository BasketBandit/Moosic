package com.basketbandit.moosic.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LavaPlayer {
    private static final Logger log = LoggerFactory.getLogger(LavaPlayer.class);
    private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    private final AudioPlayer player = manager.createPlayer();
    private final AudioTrackScheduler audioTrackScheduler = new AudioTrackScheduler(manager, player);
    private final AudioLoadHandler audioLoadHandler = new AudioLoadHandler(manager, audioTrackScheduler);

    public LavaPlayer() {
        AudioSourceManagers.registerRemoteSources(this.manager);
        this.player.setVolume(50);
        this.player.addListener(audioTrackScheduler);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public AudioTrackScheduler getAudioTrackScheduler() {
        return audioTrackScheduler;
    }

    public AudioLoadHandler getAudioLoadHandler() {
        return audioLoadHandler;
    }
}
