package org.frikis.zayer.swimmingwithpebble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent myIntent = new Intent(context, MainService.class);
        context.startService(myIntent);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
