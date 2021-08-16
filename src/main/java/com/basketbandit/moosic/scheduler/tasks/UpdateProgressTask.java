package com.basketbandit.moosic.scheduler.tasks;

import com.basketbandit.moosic.player.AudioTrackScheduler;
import com.basketbandit.moosic.scheduler.Task;
import com.basketbandit.moosic.socket.FacadeWebSocketSession;
import com.basketbandit.moosic.socket.SocketHandler;
import org.springframework.web.socket.TextMessage;

public class UpdateProgressTask implements Task {
    private final SocketHandler socketHandler;
    private final AudioTrackScheduler scheduler;
    private final FacadeWebSocketSession facade = new FacadeWebSocketSession();

    public UpdateProgressTask(SocketHandler socketHandler, AudioTrackScheduler scheduler) {
        this.socketHandler = socketHandler;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        if(scheduler.getActiveTrack() != null) {
            socketHandler.handleTextMessage(facade, new TextMessage("t:" + scheduler.getActiveTrackProgress()));
        }
    }

}
