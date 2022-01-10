package com.aware.plugin.probadva;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ContextReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        /*Cursor valuesLocation = getContentResolver().query(Locations_Provider.Locations_Data.CONTENT_URI, null, null, null, Locations_Provider.Locations_Data.TIMESTAMP + " DESC LIMIT 1");
        if(valuesLocation != null && valuesLocation.moveToFirst() ) {
            System.out.println("Current latitute" + valuesLocation.getDouble(valuesLocation.getColumnIndex(Locations_Provider.Locations_Data.LATITUDE));
            System.out.println("Current longitute" + valuesLocation.getDouble(valuesLocation.getColumnIndex(Locations_Provider.Locations_Data.LONGITUDE));
        }
        if(valuesLocation != null && ! valuesLocation.isClosed()) valuesLocation.close();
*/
    }
}
