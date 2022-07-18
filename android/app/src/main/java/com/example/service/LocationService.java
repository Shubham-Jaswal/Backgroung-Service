package com.example.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.service.Utils.Utils;

import java.util.Calendar;

public class LocationService extends Service {
    public LocationService() {
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent intent1 = new Intent(this, LocationService.class);
            PendingIntent pi = PendingIntent.getService(this, 0, intent1, PendingIntent.FLAG_NO_CREATE);
            //getting current time and add 5 seconds to it
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 20);
//registering our pending intent with alarmmanager
           /* AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);*/
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Utils.FIRST_CHANNEL_ID);
            Notification notification = builder.setOngoing(true)
                   // .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentTitle(Utils.FIRST_NOTIFICATION_TITLE)
                    .setContentText(Utils.FIRST_NOTIFICATION_DEC)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
            startForeground(101, notification);
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "no perms", Toast.LENGTH_LONG).show();
            }

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Toast.makeText(getApplicationContext(), Double.toString(location.getLatitude()), Toast.LENGTH_LONG).show();
                    System.out.println(location.getLatitude());
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    LocationListener.super.onProviderEnabled(provider);
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    LocationListener.super.onProviderDisabled(provider);
                }
            });


            /*Calendar c = Calendar.getInstance();
            Calendar clone = (Calendar) c.clone();
            clone.set(Calendar.MINUTE,c.get(Calendar.MINUTE)+1);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(this, LocationService.class);
            PendingIntent pi = PendingIntent.getService(this,0,intent1,PendingIntent.FLAG_NO_CREATE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,clone.getTimeInMillis(),pi);*/
            Toast.makeText(this, "Hi there", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}