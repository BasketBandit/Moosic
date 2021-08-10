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
    private final AudioTrackScheduler scheduler = new AudioTrackScheduler(manager, player);

    public LavaPlayer() {
        AudioSourceManagers.registerRemoteSources(this.manager);
        new AudioSendHandler(manager, player).start();
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public AudioTrackScheduler getAudioTrackScheduler() {
        return scheduler;
    }

    public void load(String url) {
        scheduler.load(url);
    }
}
