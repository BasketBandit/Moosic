package com.basketbandit.moosic.player;

import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats.COMMON_PCM_S16_BE;

public class AudioSendHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AudioSendHandler.class);
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private AudioPlayerManager manager;
    private AudioPlayer player;

    public AudioSendHandler(AudioPlayerManager manager, AudioPlayer player) {
        try {
            this.manager = manager;
            this.manager.getConfiguration().setOutputFormat(COMMON_PCM_S16_BE);
            this.player = player;
        } catch(Exception e) {
            log.error("There was a problem initialising ThreadedAudioSendHandler: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    public void start() {
        executor.execute(this);
    }

    public void run() {
        try(AudioInputStream stream = AudioPlayerInputStream.createStream(this.player, this.manager.getConfiguration().getOutputFormat(), 10000L, true);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, stream.getFormat()))) {

            line.open(stream.getFormat());
            line.start();

            byte[] buffer = new byte[COMMON_PCM_S16_BE.maximumChunkSize()];
            int chunkSize;

            while((chunkSize = stream.read(buffer)) > 0) {
                line.write(buffer, 0, chunkSize);
            }
        } catch(Exception e) {
            log.error("There was a problem playing the track: {}", e.getMessage(), e);
        }
    }
}
