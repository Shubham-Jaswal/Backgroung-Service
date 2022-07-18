package com.example.service;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),"backgroundservice").setMethodCallHandler(
                (call, result) -> {
                    if(call.method.equals("startservice"))
                    {

                        WorkManager workmanger = WorkManager.getInstance(this);

                        PeriodicWorkRequest periodicWorkRequest= new PeriodicWorkRequest.Builder(Workmanager.class
                        ,PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS).build();
                        workmanger.enqueueUniquePeriodicWork("aaaaaa",ExistingPeriodicWorkPolicy.REPLACE,
                                periodicWorkRequest);
                       /* OneTimeWorkRequest blurBuilder =
                                new OneTimeWorkRequest.Builder(Workmanager.class).build();
                        workmanger.enqueue(blurBuilder);*/


                          /*  Intent intent=new Intent(this, LocationService.class);
                            startService(intent);
                            createFirstNotification();*/


                    }
                }
        );
    }

    public void createFirstNotificationChannel(View view){

    }

    public void createFirstNotification(){

    }
}
