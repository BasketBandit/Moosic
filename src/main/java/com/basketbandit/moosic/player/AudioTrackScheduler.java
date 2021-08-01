package com.basketbandit.moosic.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AudioTrackScheduler extends AudioEventAdapter {
    private static final Logger log = LoggerFactory.getLogger(AudioTrackScheduler.class);
    private final AudioSendHandler audioSendHandler;
    private final AudioPlayerManager manager;
    private final AudioPlayer player;
    private final Queue<AudioTrack> queue = new LinkedList<>();
    private final Deque<AudioTrack> history = new ArrayDeque<>(); // Originally wanted to use java.util.Stack but despite being LIFO the .foreach doesn't respect that ordering whereas java.util.Deque does.
    private AudioTrack last;
    private AudioTrack pausedTrack;

    public AudioTrackScheduler(AudioPlayerManager manager, AudioPlayer player) {
        this.manager = manager;
        this.player = player;
        this.audioSendHandler = new AudioSendHandler(manager, player);
        this.audioSendHandler.start();
    }

    public Queue<AudioTrack> getQueue() {
        return queue;
    }

    public Deque<AudioTrack> getHistory() {
        return history;
    }

    public AudioTrack getCurrentTrack() {
        return (player.getPlayingTrack() != null) ? player.getPlayingTrack() : (pausedTrack != null && player.isPaused()) ? pausedTrack : null;
    }

    @SuppressWarnings("unchecked")
    public double getCurrentTrackProgress() {
        if(getCurrentTrack() != null) {
            ((HashMap<String, String>) getCurrentTrack().getUserData()).put("position", AudioLoadHandler.toTime(getCurrentTrack().getPosition()));
            return (double) Math.round(((getCurrentTrack().getPosition()+.0)/(getCurrentTrack().getDuration()+.0)*100) * 100) / 100;
        }
        return 0.0;
    }

    public AudioTrack getLastTrack() {
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

    @Override
    public void onPlayerPause(AudioPlayer player) {
        AudioTrack track = getCurrentTrack().makeClone();
        track.setPosition(getCurrentTrack().getPosition());
        pausedTrack = track;
        player.stopTrack();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        player.playTrack(pausedTrack);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        log.info("Playing track: {}", track.getInfo().title);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            pausedTrack = null;
            history.push(last = track);
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
