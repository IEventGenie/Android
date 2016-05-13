package com.beacons.app.beaconsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.notificationDb.BeaconNotification;
import com.beacons.app.notificationDb.DatabaseHandler;
import com.pushwoosh.PushManager;
import com.pushwoosh.internal.PushManagerImpl;

import org.json.JSONObject;

public class NotificationReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {
        if (intent == null)
            return;
 
        //Let Pushwoosh SDK to pre-handling push (Pushwoosh track stats, opens rich pages, etc.).
        //It will return Bundle with a push notification data
        Bundle pushBundle = PushManagerImpl.preHandlePush(context, intent);
        if(pushBundle == null)
            return;
             
        //get push bundle as JSON object
        JSONObject dataObject = PushManagerImpl.bundleToJSON(pushBundle);

        Log.e("Customized", "Notification ::: " + dataObject.toString());
        try{
            DatabaseHandler dbHandler = new DatabaseHandler(context);
            BeaconNotification noti = new BeaconNotification();
            JSONObject job = new JSONObject(dataObject.toString());
            String title = job.getString("title");
            noti.setTitle(title);
            noti.setType(GlobalConstants.PUSHWOOSH_TYPE_PUSH_NOTIFICATION);
            dbHandler.addNotification(noti);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Get default launcher intent for clarity
        Intent launchIntent  = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launchIntent.addCategory("android.intent.category.LAUNCHER");
             
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
 
        //Put push notifications payload in Intent
        launchIntent.putExtras(pushBundle);
        launchIntent.putExtra(PushManager.PUSH_RECEIVE_EVENT, dataObject.toString());
 
        //Start activity!
        context.startActivity(launchIntent);
         
        //Let Pushwoosh SDK post-handle push (track stats, etc.)
        PushManagerImpl.postHandlePush(context, intent);
    }
}