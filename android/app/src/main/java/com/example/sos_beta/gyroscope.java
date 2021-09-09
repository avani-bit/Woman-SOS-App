package com.example.sos_beta;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import io.flutter.app.FlutterApplication;

public class gyroscope extends FlutterApplication {
    public interface Listener{
        void onRotation(float x, float y, float z);
    }
    public Listener listener;
    public void setListener(Listener l)
    {
        listener = l;
    }
    final private SensorManager sensorManager;
    final private Sensor sensor;
    final private SensorEventListener sensorEventListener;

    gyroscope(Context context)
    {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(listener!= null)
                {
                    listener.onRotation(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    public void register()
    {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void unregister()
    {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
