package com.aware.plugin.probadva;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Light;
import com.aware.Proximity;
import com.aware.providers.Battery_Provider;
import com.aware.providers.Light_Provider;
import com.aware.providers.Locations_Provider;
import com.aware.providers.Proximity_Provider;
import com.aware.providers.Telephony_Provider;
import com.aware.utils.Aware_Plugin;

import java.util.Calendar;

public class Plugin extends Aware_Plugin {
    public static double CURRENT_LATITUDE, CURRENT_LONGITUDE;


    public ContextReceiver dataReceiver = new ContextReceiver();

    private static Intent aware;
    private static ContextProducer sContext;
    Aware_Plugin aware_plugin = new Aware_Plugin();

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("jsdfbhkshdfkshfksbksbjsf tuuuuuuuuuu");
        aware = new Intent(this, Aware.class);
        startService(aware);

        TAG = "Big Brother";
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");
        if (DEBUG) Log.d(TAG, "Probadva-plugin running");



        IntentFilter filter = new IntentFilter();

        registerReceiver(dataReceiver, filter);

//Shares this plugin's context to AWARE and applications
        sContext = new ContextProducer() {
            @Override
            public void onContext() {

                System.out.println("jsdfbhkshdfkshfksbksbjsf tuuuuuuuuuu");
                ContentValues context_data = new ContentValues();
                context_data.put(Provider.Example_Data.TIMESTAMP, System.currentTimeMillis());
                context_data.put(Provider.Example_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
                if( DEBUG ) Log.d(TAG, context_data.toString());
//insert data to table
                getContentResolver().insert(Provider.Example_Data.CONTENT_URI, context_data);

            }
        };
        CONTEXT_PRODUCER = sContext;
//Our provider tables


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Set Sensors Off

        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_NETWORK, false);

        Intent refresh = new Intent(Aware.ACTION_AWARE_CURRENT_CONTEXT);
        sendBroadcast(refresh);

        unregisterReceiver(dataReceiver);

        stopService(aware);

    }

    public class ContextReceiver extends BroadcastReceiver {

        @SuppressLint("Range")
        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("jsdfbhkshdfkshfksbksbjsf tuuuuuuuuuu");
//Location
            Cursor valuesLocation = getContentResolver().query(Locations_Provider.Locations_Data.CONTENT_URI, null, null, null, Locations_Provider.Locations_Data.TIMESTAMP + " DESC LIMIT 1");
            if(valuesLocation != null && valuesLocation.moveToFirst() ) {
                CURRENT_LATITUDE = valuesLocation.getDouble(valuesLocation.getColumnIndex(Locations_Provider.Locations_Data.LATITUDE));
                CURRENT_LONGITUDE = valuesLocation.getDouble(valuesLocation.getColumnIndex(Locations_Provider.Locations_Data.LONGITUDE));
            }
            if(valuesLocation != null && ! valuesLocation.isClosed()) valuesLocation.close();

//Share context
            sContext.onContext();
        }

    }

}
