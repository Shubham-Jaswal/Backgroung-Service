package com.example.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.service.Utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import io.flutter.plugin.common.MethodChannel;

public class LocationService extends Service {
    public LocationService() {
    }

    String wifiname = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            isAirplaneModeOn(getApplicationContext());


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Utils.FIRST_CHANNEL_ID);
            Notification notification = builder.setOngoing(true)
                    //.setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentTitle(Utils.FIRST_NOTIFICATION_TITLE)
                    .setContentText(Utils.FIRST_NOTIFICATION_DEC)
                    .setPriority(NotificationCompat.PRIORITY_LOW).build();
            startForeground(101, notification);


            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

            HandlerThread th = new HandlerThread("ankit");
            th.start();


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Please Enable Location Permission", Toast.LENGTH_LONG).show();
            }
            lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {


                    SQLiteDB ob = new SQLiteDB(getApplicationContext(), "", null, 22);
                    SQLiteDatabase db = ob.getWritableDatabase();

                    BatteryManager bm = (BatteryManager) getApplicationContext().getSystemService(BATTERY_SERVICE);
                    int batlevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);



                    ConnectivityManager cm =
                            (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    assert cm != null;
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (wifi.isConnected()) {
                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        wifiname = wifiInfo.getSSID();
                    }

                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnected();


                    if (isConnected) {
                        try {
                            HttpURLConnection urlc = (HttpURLConnection)
                                    (new URL("http://clients3.google.com/generate_204")
                                            .openConnection());
                            urlc.setRequestProperty("User-Agent", "Android");
                            urlc.setRequestProperty("Connection", "close");
                            urlc.setConnectTimeout(1500);
                            urlc.connect();
                            if (urlc.getResponseCode() == 204 &&
                                    urlc.getContentLength() == 0) {
                                System.out.println("Connected to Internet");
                                Toast.makeText(getApplicationContext(), "Connected to Internet" + String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();
                                URL url = new URL("https://onpress.000webhostapp.com/index.php?time=" + String.valueOf(location.getLatitude()) + Calendar.getInstance().getTime() + "&name=" + String.valueOf(wifiname));
                                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                                InputStream inputStream = httpsURLConnection.getInputStream();
                                Cursor cursor = db.rawQuery("SELECT * FROM LocationData", null);
                                if (cursor != null) {
                                    System.out.println("DataBase is not Empty");
                                    db.close();
                                }

                            }
                        } catch (IOException e) {
                            Log.e("TAG", "Error checking internet connection", e);
                        }
                    } else {
                        Log.d("TAG", "No network available!");
                        try {

                            ContentValues cv = new ContentValues();
                            cv.put("latitude", String.valueOf(location.getLatitude()));
                            cv.put("longitude", String.valueOf(location.getLongitude()));
                            if(bm.isCharging()) {
                                cv.put("battery", batlevel + "% AC PluggedIn" );
                            }
                            else
                            {
                                cv.put("battery", batlevel + "% AC PluggedOut" );
                            }
                            db.insert("LocationData", "latitude", cv);
                            db.close();

                            db = ob.getReadableDatabase();
                            Cursor cursor = db.rawQuery("SELECT * FROM LocationData", null);
                            cursor.moveToFirst();
                            db.close();

                            db=ob.getReadableDatabase();
                            Cursor cursor1 = db.rawQuery("SELECT * FROM LocationData", null);
                            List<String> fileName = new ArrayList<>();
                            if (cursor1.moveToFirst()){
                                fileName.add(String.valueOf(cursor1.getColumnIndex("latitude")));
                                while(cursor1.moveToNext()){
                                    fileName.add(String.valueOf(cursor1.getColumnIndex("longitude")));
                                }
                            }
                            for (int i=0;i<=fileName.size();i++)
                            {
                                System.out.println(i);
                            }
                            cursor.close();
                            db.close();






                            Toast.makeText(getApplicationContext(), String.valueOf(cursor.getString(2)), Toast.LENGTH_LONG).show();

                            db = ob.getWritableDatabase();
                            db.execSQL("DELETE FROM LocationData");
                            db.close();


                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }

                    stopForeground(true);
                    stopSelf();
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {

                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            }, th.getLooper());
        }

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }


}