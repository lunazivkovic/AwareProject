package com.aware.plugin.probadva;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.aware.utils.Aware_Plugin;
import com.aware.utils.IContextCard;

import java.util.Calendar;

public class ContextCard implements IContextCard {

    //Set how often your card needs to refresh if the stream is visible (in milliseconds)
    private int refresh_interval = 1 * 1000; //1 second = 1000 milliseconds

    private Handler uiRefresher = new Handler(Looper.getMainLooper());
    private Runnable uiChanger = new Runnable() {
        @Override
        public void run() {
            System.out.println("blablaaa");
            if (card != null) {

//Get values from database and put in strings:
                //private double CURRENT_LIGHT, CURRENT_PROXI, CURRENT_SIGNAL1, CURRENT_SIGNAL2, CURRENT_MAGNET, CURRENT_BATTEMP, CURRENT_IN, CURRENT_OUT, IN_TIME = 0.0, OUT_TIME = 0.0, inminute = 0.0, outminute = 0.0;

                Cursor values = sContext.getContentResolver().query(Provider.Example_Data.CONTENT_URI, null, null, null, Provider.Example_Data.TIMESTAMP + " DESC LIMIT 1");
                if (values != null && values.moveToFirst()) {


//Puts in/out times in more fancy format

                }
                if (values != null && !values.isClosed()) values.close();
            }
//Reset timer and schedule the next card refresh
            uiRefresher.postDelayed(uiChanger, refresh_interval);
        }
    };


    //Empty constructor used to instantiate this card
    public ContextCard() {}

    //You may use sContext on uiChanger to do queries to databases, etc.
    private Context sContext;

    //Declare here all the UI elements you'll be accessing
    private View card;

    //Used to load your context card
    private LayoutInflater sInflater;

    private TextView signalText1, signalText2, lightText, battText, proxiText, inText, outText, intimeText, outtimeText;

//Some variables used

    @Override
    public View getContextCard(Context context) {

        System.out.println("eeeee");
        sContext = context;

//Tell Android that you'll monitor the stream statuses
        IntentFilter filter = new IntentFilter();
        context.registerReceiver(streamObs, filter);

//Inflate and return your card's layout. See LayoutInflater documentation.
        sInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        uiRefresher.post(uiChanger);

        return card;
    }


    private StreamObs streamObs = new StreamObs();

    public class StreamObs extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("aaaaaaaaaaaaaa");

//start refreshing when user enters the stream
                uiRefresher.postDelayed(uiChanger, refresh_interval);


//stop refreshing when user leaves the stream
                uiRefresher.removeCallbacks(uiChanger);
                uiRefresher.removeCallbacksAndMessages(null);

        }
    }
}
