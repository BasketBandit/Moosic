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
    private final LinkedList<AudioTrack> queue = new LinkedList<>();
    private final Deque<AudioTrack> history = new ArrayDeque<>(); // Originally wanted to use java.util.Stack but despite being LIFO the .foreach doesn't respect that ordering whereas java.util.Deque does.
    private AudioTrack pausedTrack;

    public AudioTrackScheduler(AudioPlayerManager manager, AudioPlayer player) {
        this.manager = manager;
        this.player = player;
        this.audioSendHandler = new AudioSendHandler(manager, player);
        this.audioSendHandler.start();
    }

    public List<AudioTrack> getQueue() {
        return queue;
    }

    public Deque<AudioTrack> getHistory() {
        return history;
    }

    public AudioTrack getActiveTrack() {
        return (player.getPlayingTrack() != null) ? player.getPlayingTrack() : (pausedTrack != null && player.isPaused()) ? pausedTrack : null;
    }

    @SuppressWarnings("unchecked")
    public double getActiveTrackProgress() {
        if(getActiveTrack() != null) {
            ((HashMap<String, String>) getActiveTrack().getUserData()).put("position", AudioLoadHandler.toTime(getActiveTrack().getPosition()));
            return (double) Math.round(((getActiveTrack().getPosition()+.0)/(getActiveTrack().getDuration()+.0)*100) * 100) / 100;
        }
        return 0.0;
    }

    public void queue(AudioTrack track) {
        log.info("Queued track: {}", track.getInfo().title);
        queue.offer(track);
        if(player.getPlayingTrack() == null) {
            nextTrack();
        }
    }

    public void nextTrack() {
        if(!player.isPaused()) {
            AudioTrack track = queue.poll();
            player.startTrack(track, false);
        }
    }

    public void move(int index, int destination) {
        queue.add(destination, queue.remove(index));
    }

    public void shuffle() {
        Collections.shuffle(queue);
    }

    public void remove(int index) {
        queue.remove(Math.max(0, Math.min((queue.size()-1), index)));
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        if(player.getPlayingTrack() != null) {
            AudioTrack track = player.getPlayingTrack().makeClone();
            track.setPosition(player.getPlayingTrack().getPosition());
            pausedTrack = track;
            player.stopTrack();
        }
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        if(pausedTrack != null) {
            player.playTrack(pausedTrack);
            pausedTrack = null;
            return;
        }
        nextTrack();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        log.info("Playing track: {}", track.getInfo().title);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            pausedTrack = null;
            history.push(track);
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
