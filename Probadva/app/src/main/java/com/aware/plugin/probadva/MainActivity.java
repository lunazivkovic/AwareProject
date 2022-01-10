package com.aware.plugin.probadva;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import androidx.core.app.ActivityCompat;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Locations_Provider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    private FusedLocationProviderClient fusedLocationClient;
    DatabaseHelper db;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
       /* addPreferencesFromResource(R.xml.preferences);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        syncSettings();*/

        /*plugin plugin = new plugin();
        plugin.onCreate();
        Aware.startPlugin(getApplicationContext(), getPackageName());*/

        ContentValues new_data = new ContentValues();
        new_data.put(Provider.Example_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
        new_data.put(Provider.Example_Data.TIMESTAMP, System.currentTimeMillis());
        getContentResolver().insert(Provider.Example_Data.CONTENT_URI, new_data);

        Plugin plugin = new Plugin();
        plugin.onCreate();
       /* Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_LOCATION_NETWORK, 60);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_GPS, 60);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_NETWORK, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_GPS, true);
        System.out.println("U pluginu sm");
        Intent refresh = new Intent(Aware.ACTION_AWARE_REFRESH);
        sendBroadcast(refresh);

        Cursor valuesLocation = getContentResolver().query(Locations_Provider.Locations_Data.CONTENT_URI, null, null, null, Locations_Provider.Locations_Data.TIMESTAMP + " DESC LIMIT 1");

        System.out.println("ldfjlfjlif" + valuesLocation);
        if(valuesLocation != null && valuesLocation.moveToFirst() ) {
            System.out.println("Current latitute" + valuesLocation.getDouble(valuesLocation.getColumnIndex(Locations_Provider.Locations_Data.LATITUDE)));
            System.out.println("Current longitute" + valuesLocation.getDouble(valuesLocation.getColumnIndex(Locations_Provider.Locations_Data.LONGITUDE)));
        }
        if(valuesLocation != null && ! valuesLocation.isClosed()) valuesLocation.close();
       fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    System.out.println(location);

                }
            }
        });

    }

    private void syncSettings() {
        CheckBoxPreference status = (CheckBoxPreference) findPreference(STATUS_PLUGIN_INOROUT);
        status.setChecked(Aware.getSetting(getApplicationContext(), STATUS_PLUGIN_INOROUT).equals("true"));
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference setting = (Preference) findPreference(key);

        if( setting.getKey().equals(STATUS_PLUGIN_INOROUT) ) {
            boolean is_active = sharedPreferences.getBoolean(key, false);
            Aware.setSetting(getApplicationContext(), key, is_active);
            if( is_active ) {
                Aware.startPlugin(getApplicationContext(), getPackageName());
            } else {
                Aware.stopPlugin(getApplicationContext(), getPackageName());
            }
        }

        Intent refresh = new Intent(Aware.ACTION_AWARE_REFRESH);
        sendBroadcast(refresh);*/
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}

