package com.example.sos_beta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ScreenReceiver extends BroadcastReceiver {
    public boolean wasScreenOn = true;
    public int count = 0;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        context.sendBroadcast(new Intent("POWER_BUTTON"));
        Log.i("LOB","onReceive");
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_OFF:
                count++;
                wasScreenOn = false;
                Log.i("LOB", "wasScreenOn" + wasScreenOn);
                break;
            case Intent.ACTION_SCREEN_ON:
                count++;
                wasScreenOn = true;

                break;
            case Intent.ACTION_USER_PRESENT:
                Log.i("LOB", "userpresent");
                Log.i("LOB", "wasScreenOn" + wasScreenOn);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
        }

        if(count==8) {
            Log.e("info", "pressed 4 times");
        }
    }
}


