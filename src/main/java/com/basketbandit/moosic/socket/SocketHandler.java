package com.basketbandit.moosic.socket;

import com.basketbandit.moosic.Moosic;
import com.basketbandit.moosic.player.AudioTrackScheduler;
import com.basketbandit.moosic.player.LavaPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);
    private static final ArrayList<WebSocketSession> clients = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            if(message.getPayload().startsWith("action:")) {
                LavaPlayer lavaPlayer = Moosic.lavaPlayer;
                AudioPlayer player = lavaPlayer.getPlayer();
                AudioTrackScheduler scheduler = lavaPlayer.getAudioTrackScheduler();

                String[] action = message.getPayload().split(",");
                String command = action[0].split(":")[1];
                switch(command) {
                    case "load" -> {
                        String value = action[1].split(":", 2)[1]; // limit 2 to stop colons in urls breaking things
                        Moosic.lavaPlayer.load(value);
                    }
                    case "play" -> player.setPaused(!player.isPaused());
                    case "skip" -> {
                        if(scheduler.getActiveTrack() != null) {
                            scheduler.onTrackEnd(player, scheduler.getActiveTrack(), AudioTrackEndReason.FINISHED);
                        }
                    }
                    case "shuffle" -> scheduler.shuffle();
                    case "remove" -> {
                        String value = action[1].split(":", 2)[1];
                        scheduler.remove(Integer.parseInt(value));
                    }
                    case "move" -> {
                        String value = action[1].split(":", 2)[1];
                        String extra = action[2].split(":", 2)[1];
                        scheduler.move(Integer.parseInt(value), Integer.parseInt(extra));
                    }
                    case "volume" -> {
                        String value = action[1].split(":", 2)[1];
                        Moosic.lavaPlayer.getPlayer().setVolume(Math.min(100, Math.max(0, Integer.parseInt(value))));
                    }
                    case "clearQueue" -> scheduler.getQueue().clear();
                    case "clearHistory" -> scheduler.getHistory().clear();
                }
            }

            for(WebSocketSession client : clients) {
                client.sendMessage(message);
            }
        } catch(Exception e) {
            log.error("There was an error handling message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        clients.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        clients.remove(session);
    }
}
