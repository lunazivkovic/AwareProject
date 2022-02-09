package com.aware.plugin.probadva;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.aware.Aware;

import com.aware.ESM;
import com.aware.plugin.google.activity_recognition.Google_AR_Provider;
import com.aware.providers.ESM_Provider;
import com.aware.providers.Locations_Provider;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String STATUS_PLUGIN_PROBADVA = "status_plugin_probadva";
    public static final String SURVEY_ANSWERS = "survey_answers";
    public static String ANSWERR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        Aware.startAWARE(getApplicationContext());
        syncSettings();


    }

    private void syncSettings() {
        CheckBoxPreference status = (CheckBoxPreference) findPreference(STATUS_PLUGIN_PROBADVA);
        status.setChecked(Aware.getSetting(getApplicationContext(), STATUS_PLUGIN_PROBADVA).equals("true"));
        CheckBoxPreference statusSurvey = (CheckBoxPreference) findPreference(SURVEY_ANSWERS);
        statusSurvey.setChecked(Aware.getSetting(getApplicationContext(), SURVEY_ANSWERS).equals("true"));
    }


    @SuppressLint("Range")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference setting = (Preference) findPreference(key);

        if (setting.getKey().equals(STATUS_PLUGIN_PROBADVA)) {
            boolean is_active = sharedPreferences.getBoolean(key, false);
            Aware.setSetting(getApplicationContext(), key, is_active);
            if (is_active) {
                Aware.startPlugin(getApplicationContext(), getPackageName());

            } else {

                Aware.stopPlugin(getApplicationContext(), getPackageName());

            }

        }

        if (setting.getKey().equals(SURVEY_ANSWERS)) {
            boolean is_active = sharedPreferences.getBoolean(key, false);
            Aware.setSetting(getApplicationContext(), key, is_active);
            if (is_active) {
               // Cursor esmActivity = getContentResolver().query(ESM_Provider.ESM_Data.CONTENT_URI, null, null, null, ESM_Provider.ESM_Data.TIMESTAMP + " DESC LIMIT 1");
                Cursor esmActivity = getContentResolver().query(ESM_Provider.ESM_Data.CONTENT_URI, null, null, null, ESM_Provider.ESM_Data.TIMESTAMP + " DESC LIMIT 1");
               // ANSWERR = (esmActivity.(ESM_Provider.ESM_Data.ANSWER));
                //ANSWERR = esmActivity.getString(esmActivity.getColumnIndex(ESM_Provider.ESM_Data.ANSWER));


                if(esmActivity != null && esmActivity.moveToFirst() ) {

                    ANSWERR = esmActivity.getString(esmActivity.getColumnIndex(ESM_Provider.ESM_Data.ANSWER));

                }
                //ANSWER = ESM_Provider.ESM_Data.ANSWER;
                System.out.println("Answer: " + ANSWERR);



            } else {


                System.out.println("Ugasila sam answers");
            }

        }

        Intent refresh = new Intent(Aware.ACTION_AWARE_SYNC_DATA);
        sendBroadcast(refresh);
    }

}