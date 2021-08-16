package com.basketbandit.moosic.scheduler.jobs;

import com.basketbandit.moosic.scheduler.Job;
import com.basketbandit.moosic.scheduler.tasks.UpdateProgressTask;

import java.util.concurrent.TimeUnit;

public class UpdateJob extends Job {
    private final UpdateProgressTask task;

    public UpdateJob(UpdateProgressTask task) {
        super(0, 100, TimeUnit.MILLISECONDS);
        this.task = task;
    }

    @Override
    public void run() {
        handleTask(task);
    }
}
