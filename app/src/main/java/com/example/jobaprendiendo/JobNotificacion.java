package com.example.jobaprendiendo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


public class JobNotificacion extends JobService {
    @Override
    public boolean onStartJob(final JobParameters params) {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e){
                    Log.e("JobNotificacion", "Hilpo interrumpido, hubo error", e);
                }

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JobNotificacion.this, "JobNotificacion ejecutado !", Toast.LENGTH_SHORT).show();
                    }

                });
                jobFinished(params, false);
            }
        });

        backgroundThread.start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
