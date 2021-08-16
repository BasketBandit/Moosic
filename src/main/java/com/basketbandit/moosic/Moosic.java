package com.basketbandit.moosic;

import com.basketbandit.moosic.player.LavaPlayer;
import com.basketbandit.moosic.scheduler.ScheduleHandler;
import com.basketbandit.moosic.scheduler.jobs.UpdateJob;
import com.basketbandit.moosic.scheduler.tasks.UpdateProgressTask;
import com.basketbandit.moosic.socket.SocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Moosic {
	public static final SocketHandler socketHandler = new SocketHandler();
	public static final LavaPlayer lavaPlayer = new LavaPlayer(); // I don't like making public static fields like this.

	public Moosic() {
		ScheduleHandler.registerJob(new UpdateJob(new UpdateProgressTask(socketHandler, lavaPlayer.getAudioTrackScheduler())));
	}

	public static void main(String[] args) {
		SpringApplication.run(Moosic.class, args);
	}
}
