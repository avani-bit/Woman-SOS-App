package com.example.sos_beta;

import android.Manifest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    public gyroscope gyro;
    FusedLocationProviderClient fusedLocationProviderClient;
    public String userLocation;
    public int flag = 0;
    Location location;


        @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "backgroundService")
                .setMethodCallHandler(
                        ((call, result) -> {

                            if (call.method.equals("start")) {
                                Intent i = new Intent(getApplicationContext(), MyService.class);
                                startService(i);
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                                    requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 100);

                                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                                getLastLocation();
                                registerReceiver(broadcastReceiver, new IntentFilter("POWER_BUTTON"));

                               // checkShake();
                            }
                            result.success("started");
                        })
                );
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission();
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = "http://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+1-555-521-5554", null, "Iam in danger, Help me! My location: " + userLocation, null, null);
                Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_SHORT).show();
                Log.i("loc", userLocation);
            } else
                Log.i("loc", "null loc");
        });


    }

    public void askPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS}, 100);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLastLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       // gyro.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // gyro.unregister();
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           getLastLocation();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

     public void checkShake() {
         gyro = new gyroscope(getApplicationContext());
         gyro.setListener((x, y, z) -> {
            if (flag == 0) {
                if (z > 1.0f || z < -1.0f || y > 1.0f || y < -1.0f || x > 1.0f || x < -1.0f) {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                    //flag = 1;
                    //gyro.unregister();
                    getLastLocation();

                }
            }
        });
    }

    public ComponentName startService(Intent i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(i);
        } else {
            startService(i);
        }
        return null;
    }

}

