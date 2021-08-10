package com.basketbandit.moosic.socket;

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
