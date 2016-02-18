package com.beacons.app.beacons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beacons.app.beaconsapp.HomeActivity;
import com.beacons.app.beaconsapp.R;
import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;

public class BeaconstacBroadcastReceiver extends BeaconstacReceiver {
    private NotificationManager notificationManager;

    @Override
    public void exitedBeacon(Context context, MSBeacon beacon) {
        Log.v(BeaconstacBroadcastReceiver.class.getName(), "exited called " + beacon.getBeaconKey());
        sendNotification(context, "Exited " + beacon.getMajor() + " : " + beacon.getMinor());
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {
        Log.v(BeaconstacBroadcastReceiver.class.getName(), "Ranged called " + beacons.size());
        sendNotification(context, "Ranged " + beacons.size() + " beacons");
    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon beacon) {
        Log.v(BeaconstacBroadcastReceiver.class.getName(), "camped on called " + beacon.getBeaconKey());
        sendNotification(context, "Camped " + beacon.getMajor() + " : " + beacon.getMinor());
    }

    @Override
    public void triggeredRule(Context context, String ruleName, ArrayList<MSAction> actions) {
        Log.v(BeaconstacBroadcastReceiver.class.getName(), "triggered rule called " + ruleName);
    }

    @Override
    public void enteredRegion(Context context, String region) {
        Log.v(BeaconstacBroadcastReceiver.class.getName(), "Entered region " + region);
    }

    @Override
    public void exitedRegion(Context context, String region) {
        notificationManager = (NotificationManager)
                context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Log.v(BeaconstacBroadcastReceiver.class.getName(), "Exited region " + region);
    }

    @Override
    public void enteredGeofence(Context context, ArrayList<MSPlace> arrayList) {
        Log.v(BeaconstacBroadcastReceiver.class.getName(), "Entered geofence");
    }

    @Override
    public void exitedGeofence(Context context, ArrayList<MSPlace> arrayList) {
        Log.v(BeaconstacBroadcastReceiver.class.getName(), "Exited geofence");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    private void sendNotification(Context context, String text) {
        if (context != null) {
            Intent activityIntent = new Intent(context.getApplicationContext(), HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context.getApplicationContext(),
                    0,
                    activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            Notification mBuilder = new Notification.Builder(context.getApplicationContext())
                    .setContentText(text)
                    .setContentTitle("BeaconstacExample")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent).build();
            notificationManager = (NotificationManager)
                    context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, mBuilder);
        }
    }
}
