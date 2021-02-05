package com.modernboyz.hotspotmanager.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class JobScheduler extends JobService {
    public static final String TAG = "JobScheduler";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        showNotification(params);
        return true;
    }

    private void showNotification(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Log.d(TAG, "run: " + i);
                    if (jobCancelled) {
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "jobFinished");
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "jobCancelled before completion");
        jobCancelled = true;
        return true;
    }
}
