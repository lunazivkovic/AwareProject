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

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends PreferenceActivity {


    //private FusedLocationProviderClient fusedLocationClient;
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

      /*  ContentValues new_data = new ContentValues();
        new_data.put(Provider.Example_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
        new_data.put(Provider.Example_Data.TIMESTAMP, System.currentTimeMillis());
        getContentResolver().insert(Provider.Example_Data.CONTENT_URI, new_data);




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



*/
    }
}

