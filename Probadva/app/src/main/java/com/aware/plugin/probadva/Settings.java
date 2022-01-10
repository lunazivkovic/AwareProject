package com.aware.plugin.probadva;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ESM;
import com.aware.providers.Aware_Provider;
import com.aware.utils.Aware_Plugin;

import java.nio.file.Path;

//Settings is copy from AWARE template
public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String STATUS_PLUGIN_PROBADVA = "status_plugin_probadva";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        System.out.println(Aware_Provider.Aware_Settings.CONTENT_URI);
        DatabaseHelper db;
        db = new DatabaseHelper(this);
        ContentValues new_data = new ContentValues();
        new_data.put(Provider.Example_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
        new_data.put(Provider.Example_Data.TIMESTAMP, System.currentTimeMillis());
        getContentResolver().insert(Provider.Example_Data.CONTENT_URI, new_data);

        syncSettings();

       // Plugin plugin = new Plugin();
        //Aware_Plugin aware_plugin = new Aware_Plugin();
        //plugin.onCreate();
        //syncSettings();
    }

    private void syncSettings() {
        CheckBoxPreference status = (CheckBoxPreference) findPreference(STATUS_PLUGIN_PROBADVA);
        status.setChecked(Aware.getSetting(getApplicationContext(), STATUS_PLUGIN_PROBADVA).equals("true"));
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference setting = (Preference) findPreference(key);

        if( setting.getKey().equals(STATUS_PLUGIN_PROBADVA) ) {

            boolean is_active = sharedPreferences.getBoolean(key, false);
            Aware.setSetting(getApplicationContext(), key, is_active);
            if( is_active ) {
                Aware.startPlugin(getApplicationContext(), getPackageName());
            } else {
                Aware.stopPlugin(getApplicationContext(), getPackageName());
            }
        }


        System.out.println("ddddd aaaaaaaaaaaaaaaaaaaaaaaa");
    }
}