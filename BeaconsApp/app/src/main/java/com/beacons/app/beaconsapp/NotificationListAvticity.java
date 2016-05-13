package com.beacons.app.beaconsapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.notificationDb.BeaconNotification;
import com.beacons.app.notificationDb.DatabaseHandler;
import com.beacons.app.slidingmenu.SlidingMenuSetup;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.BaseRegistrationReceiver;
import com.pushwoosh.PushManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 4/11/16.
 */
public class NotificationListAvticity extends FragmentActivity {

    RelativeLayout actionBar;
    Globals global;
    ListView notiList;
    AlertDialog notificationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);

        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setVisibility(View.INVISIBLE);
        actionBar.findViewById(R.id.notification_icon).setVisibility(View.INVISIBLE);
        ((TextView)actionBar.findViewById(R.id.title)).setText(getResources().getString(R.string.notification_title));

        actionBar.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationListAvticity.this.finish();
            }
        });

        global = (Globals) getApplicationContext();
        global.setFragActivity(this);

        notiList = (ListView) findViewById(R.id.notification_list);

        setNotificationAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        global.fragActivityResumed(this.getClass(), NotificationListAvticity.this);
        registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        global.fragActivityPaused();
        unregisterReceivers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        global.fragActivityDestroyed();
    }

    //Registration receiver
    BroadcastReceiver mBroadcastReceiver = new BaseRegistrationReceiver()
    {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent)
        {
            checkMessage(intent);
        }
    };

    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver()
    {
        @Override
        protected void onMessageReceive(Intent intent)
        {
            //JSON_DATA_KEY contains JSON payload of push notification.
            //showMessage("push message is json key --- " + intent.getExtras().getString(JSON_DATA_KEY));
            //Log.e("push msg json key",""+intent.getExtras().getString(JSON_DATA_KEY));
            try {
                String msg = ""+intent.getExtras().getString(JSON_DATA_KEY);
                JSONObject job = new JSONObject(msg);
                String pushTitle = job.getString("title");

                DatabaseHandler dbHandler = new DatabaseHandler(NotificationListAvticity.this);
                BeaconNotification notification = new BeaconNotification();
                notification.setType(GlobalConstants.PUSHWOOSH_TYPE_PUSH_NOTIFICATION);
                notification.setTitle(pushTitle);
                dbHandler.addNotification(notification);

                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListAvticity.this);
                builder.setTitle("Notification");
                builder.setMessage(pushTitle);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notificationDialog.dismiss();
                    }
                });
                notificationDialog = builder.create();
                notificationDialog.show();
            }catch (Exception e1){
                Log.e("exceptions : ", "" + e1.getStackTrace());
            }
        }
    };

    //Registration of the receivers
    public void registerReceivers()
    {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");

        registerReceiver(mReceiver, intentFilter, getPackageName() + ".permission.C2D_MESSAGE", null);

        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers()
    {
        //Unregister receivers on pause
        try
        {
            unregisterReceiver(mReceiver);
        }
        catch (Exception e)
        {
            // pass.
        }

        try
        {
            unregisterReceiver(mBroadcastReceiver);
        }
        catch (Exception e)
        {
            //pass through
        }
    }

    private void checkMessage(Intent intent)
    {
        if (null != intent)
        {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
            {
                showMessage("push message is recv event ::: " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
                Log.e("push msg recv event", "" + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            }
            else if (intent.hasExtra(PushManager.REGISTER_EVENT))
            {
                //showMessage("register");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
            {
                //showMessage("unregister");
            }
            else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
            {
                //showMessage("register error");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
            {
                //showMessage("unregister error");
            }

            resetIntentValues();
        }
    }

    /**
     * Will check main Activity intent and if it contains any PushWoosh data, will clear it
     */
    private void resetIntentValues()
    {
        Intent mainAppIntent = getIntent();

        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }

        setIntent(mainAppIntent);
    }

    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void setNotificationAdapter(){

        DatabaseHandler handler = new DatabaseHandler(NotificationListAvticity.this);
        ArrayList<BeaconNotification> dataList = handler.getAllNotification();
        NotificationListAdapter adapter = new NotificationListAdapter(dataList);
        notiList.setAdapter(adapter);
    }

    class NotificationListAdapter  extends BaseAdapter{

        LayoutInflater inflater;
        ArrayList<BeaconNotification> dataList;

        public NotificationListAdapter(ArrayList<BeaconNotification> dataList){
            inflater = (LayoutInflater) NotificationListAvticity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView == null){

                convertView = inflater.inflate(R.layout.notification_list_item,null);
                holder = new ViewHolder();
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.date = (TextView)convertView.findViewById(R.id.date);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(dataList.get(position).getTitle());
            holder.date.setText(dataList.get(position).getDate());

            return convertView;
        }
    }

    class ViewHolder{
        TextView title,date;
        ImageView image;
    }

}