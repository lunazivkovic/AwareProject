package com.aware.plugin.probadva;

import static com.aware.plugin.probadva.Settings.STATUS_PLUGIN_PROBADVA;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.util.StateSet;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ESM;
import com.aware.providers.ESM_Provider;
import com.aware.Light;
import com.aware.Proximity;
import com.aware.providers.Locations_Provider;
import com.aware.providers.Aware_Provider;
import com.aware.ui.esms.ESMFactory;
import com.aware.ui.esms.ESM_Checkbox;
import com.aware.ui.esms.ESM_Freetext;
import com.aware.ui.esms.ESM_PAM;
import com.aware.ui.esms.ESM_QuickAnswer;
import com.aware.ui.esms.ESM_Radio;
import com.aware.utils.Aware_Plugin;
import com.aware.plugin.google.activity_recognition.Settings;
import com.aware.plugin.google.activity_recognition.Google_AR_Provider;

import org.json.JSONException;




import java.util.ArrayList;

public class Plugin extends Aware_Plugin {
    public static double CURRENT_LATITUDE, CURRENT_LONGITUDE;
    public static int CURRENT_ACTIVITY;
    public static String ANSWER;


    public ContextReceiver dataReceiver = new ContextReceiver();

    private static Intent aware;
    private static ContextProducer sContext;
    private static ContextProducer pluginContext;
    private static String answer = "";
    private static String question = "";

    @SuppressLint("Range")
    @Override
    public void onCreate() {
        super.onCreate();

        aware = new Intent(this, Aware.class);
        startService(aware);

        TAG = "AWARE::" + getResources().getString(R.string.app_name);
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");
        if (DEBUG) Log.d(TAG, "Probadva-plugin running");

        pluginContext = new ContextProducer() {
            @Override
            public void onContext() {
                if(question!=null && answer!=null && !(question.equals("") && answer.equals(""))) {

                    // Insert values into database
                    ContentValues rowData = new ContentValues();
                    rowData.put(Provider.Example_Data.TIMESTAMP, System.currentTimeMillis());
                    rowData.put(Provider.Example_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
                    rowData.put(Provider.Example_Data.ANSWER, answer);


                    Log.d(TAG, "Sending data " + rowData.toString());
                    getContentResolver().insert(Provider.Example_Data.CONTENT_URI, rowData);
                    //broadcast?
                }
            }
        };
        CONTEXT_PRODUCER = pluginContext;


        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, false);




        if (Aware.getSetting(getApplicationContext(), STATUS_PLUGIN_PROBADVA).equals("true")) {
            Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_LOCATION_GPS, 3600);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_LOCATION_NETWORK, 3600);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_NETWORK, true);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_GPS, true);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, true);
            Aware.setSetting(getApplicationContext(), Settings.STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, true);
            Aware.setSetting(getApplicationContext(), Settings.FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, 60);
            Aware.startPlugin(getApplicationContext(), "com.aware.plugin.google.activity_recognition");

            Cursor valuesLocation = getContentResolver().query(Locations_Provider.Locations_Data.CONTENT_URI, null, null, null, Locations_Provider.Locations_Data.TIMESTAMP + " DESC LIMIT 1");
            Cursor valuesActivity = getContentResolver().query(Google_AR_Provider.Google_Activity_Recognition_Data.CONTENT_URI, null, null, null, Google_AR_Provider.Google_Activity_Recognition_Data.TIMESTAMP + " DESC LIMIT 1");
            Cursor esmActivity = getContentResolver().query(ESM_Provider.ESM_Data.CONTENT_URI, null, null, null, ESM_Provider.ESM_Data.TIMESTAMP + " DESC LIMIT 1");


            if(valuesLocation != null && valuesLocation.moveToFirst() ) {
                CURRENT_LATITUDE = valuesLocation.getDouble(valuesLocation.getColumnIndex(Locations_Provider.Locations_Data.LATITUDE));
                CURRENT_LONGITUDE = valuesLocation.getDouble(valuesLocation.getColumnIndex(Locations_Provider.Locations_Data.LONGITUDE));
                CURRENT_ACTIVITY = valuesActivity.getColumnIndex(Google_AR_Provider.Google_Activity_Recognition_Data.ACTIVITY_TYPE);

            }
            if(valuesLocation != null && ! valuesLocation.isClosed()) valuesLocation.close();
            System.out.println("Collected data: " + CURRENT_LATITUDE + CURRENT_LONGITUDE + CURRENT_ACTIVITY );

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {


            if(CURRENT_LATITUDE == 37.421998333333335) {
                if (CURRENT_ACTIVITY == 3) {

                    try {
                        ESMFactory factory = new ESMFactory();

                        //define ESM question
                        ESM_Freetext esmFreetext = new ESM_Freetext();
                        esmFreetext.setTitle("Freetext")
                                .setTrigger("CURRENT_ACTIVITY == 2")
                                .setSubmitButton("OK")
                                .setInstructions("Insert some text");

                        //add them to the factory
                        factory.addESM(esmFreetext);

                        //Queue them
                        ESM.queueESM(getApplicationContext(), factory.build());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    try {
                        ESMFactory factory = new ESMFactory();

                        ESM_Checkbox q1 = new ESM_Checkbox();
                        q1.addCheck("Option 1")
                                .addCheck("Option 2")
                                .addCheck("Other")
                                .setTitle("Checkbox")
                                .setSubmitButton("OK")
                                .setInstructions("Multiple choice is allowed");

                        ESM_Radio q2 = new ESM_Radio();
                        q2.addRadio("Eating")
                                .addRadio("Working")
                                .addRadio("Not alone")
                                .setTitle("Why is that?")
                                .setSubmitButton("Thanks!");

                        ESM_QuickAnswer q0 = new ESM_QuickAnswer();
                        q0.addQuickAnswer("Yes")
                                .addQuickAnswer("No")
                                .setTitle("Is this a good time to answer?")
                                .addFlow("Yes", q1.build())
                                .addFlow("No", q2.build());

                        factory.addESM(q0);
                        //ovde mogu da dodam jos ali ce se uvek kupiti samo poslednji odgovor

                        ESM.queueESM(getApplicationContext(), factory.build());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
                }
            }, 10000);

        } else {
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_NETWORK, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_GPS, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, false);
            Aware.setSetting(getApplicationContext(), Settings.STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION, false);
        }

        Intent refresh = new Intent(Aware.ACTION_AWARE_SYNC_DATA);
        sendBroadcast(refresh);


        IntentFilter filter = new IntentFilter();

        filter.addAction(Proximity.ACTION_AWARE_PROXIMITY);

        registerReceiver(dataReceiver, filter);


                sContext = new ContextProducer() {

            @Override
            public void onContext() {

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
