package com.basketbandit.moosic.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public class AudioTrackScheduler extends AudioEventAdapter {
    private static final Logger log = LoggerFactory.getLogger(AudioTrackScheduler.class);
    private final AudioSendHandler audioSendHandler;
    private final AudioPlayerManager manager;
    private final AudioPlayer player;
    private final Queue<AudioTrack> queue = new LinkedList<>();
    private final Queue<AudioTrack> history = new LinkedList<>();
    private AudioTrack last;

    public AudioTrackScheduler(AudioPlayerManager manager, AudioPlayer player) {
        this.manager = manager;
        this.player = player;
        this.audioSendHandler = new AudioSendHandler(manager, player);
        this.audioSendHandler.start();
    }

    public Queue<AudioTrack> getQueue() {
        return queue;
    }

    public Queue<AudioTrack> getHistory() {
        return history;
    }

    public AudioTrack getLast() {
        return last;
    }

    public void queue(AudioTrack track) {
        queue.offer(track);
        log.info("Queuing track: {}", track.getInfo().title);
        if(player.getPlayingTrack() == null) {
            nextTrack();
        }
    }

    public void nextTrack() {
        AudioTrack track = queue.poll();
        if(track != null) {
            player.startTrack(track, false);
            return;
        }
        player.stopTrack();
    }

    public boolean hasNextTrack() {
        return (queue.peek() != null);
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        // Player was paused
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        // Player was resumed
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        log.info("Playing track: {}", track.getInfo().title);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            history.offer(last = track);
            nextTrack();
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        log.error("Exception when playing track: {}", exception.getMessage(), exception);
        nextTrack();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        log.error("Track '{}' stuck, skipping.", track.getInfo().title);
        nextTrack();
    }
}
