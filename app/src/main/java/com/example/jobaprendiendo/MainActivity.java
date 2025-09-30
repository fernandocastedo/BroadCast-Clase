package com.example.jobaprendiendo;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static  final int JOB_ID = 1;
    private TextView tvMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tvMensaje = findViewById(R.id.tvMensaje);

    }
    private BroadcastReceiver airplaneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAirPlaneModeOn = intent.getBooleanExtra("State",false);
            String mensaje;
            if (isAirPlaneModeOn){
                mensaje = "Modo Avion Activado";
            } else {
                mensaje = "Modo Avion Desactivado";
            }
            Toast.makeText(context,mensaje,Toast.LENGTH_SHORT).show();
            if (tvMensaje != null) {
                tvMensaje.setText(mensaje);
            }
            JobInfo jobInfo = getJobInfo(MainActivity.this);
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (scheduler !=null){
                scheduler.cancel(JOB_ID);

                int result = scheduler.schedule(jobInfo);
                if (result == JobScheduler.RESULT_SUCCESS){
                    Toast.makeText(context,"Job Programado al cambiar a modo avion",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Fallo el programa a cambiar a modo avion", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
    private static JobInfo getJobInfo(MainActivity mainActivity) {
        ComponentName componentName = new ComponentName(mainActivity, JobNotificacion.class);

        return new JobInfo.Builder(JOB_ID,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(2000)
                .setOverrideDeadline(5000)
                .setPersisted(false)
                .build();
    }
    @Override
    public void onStart(){
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneReceiver,filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(airplaneReceiver);
    }


}