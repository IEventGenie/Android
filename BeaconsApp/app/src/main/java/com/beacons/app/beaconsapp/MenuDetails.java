package com.beacons.app.beaconsapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacons.app.WebserviceDataModels.AttendeeDetailCommonModel;
import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.notificationDb.BeaconNotification;
import com.beacons.app.notificationDb.DatabaseHandler;
import com.beacons.app.utilities.CircleTransform;
import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.BaseRegistrationReceiver;
import com.pushwoosh.PushManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aman on 3/1/16.
 */
public class MenuDetails extends BaseActivity {

    RelativeLayout actionBar;
    Globals global;
    LinearLayout detailContainer;
    ImageView eventImg;
    TextView titleTxt,location,date;
    EventDetailMainModel dataModel;
    AlertDialog notificationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_detail);

        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setVisibility(View.INVISIBLE);
        actionBar.findViewById(R.id.notification_icon).setVisibility(View.INVISIBLE);

        actionBar.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDetails.this.finish();
            }
        });

        global = (Globals) getApplicationContext();

        detailContainer = (LinearLayout) findViewById(R.id.detail_container);

        setEventData();
        setupData();
    }

    public void setEventData(){
        try {
            dataModel = global.getEventDetailMainModel();

            eventImg = (ImageView) findViewById(R.id.event_img);
            titleTxt = (TextView) findViewById(R.id.title_sec);
            location = (TextView) findViewById(R.id.location);
            date = (TextView) findViewById(R.id.date);

            titleTxt.setText("" + dataModel.detailModel.Ev_Nm);

            location.setText("" + dataModel.detailModel.Ev_Addr_1_Txt + "," + dataModel.detailModel.Ev_City_Txt);
            Picasso.with(MenuDetails.this)
                    .load("" + dataModel.detailModel.Ev_Img_Url)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.icon)
                    .into(eventImg);
            try {
                String sd = "" + dataModel.detailModel.Ev_Strt_Dt;
                sd = sd.split("T")[0];

                String ed = "" + dataModel.detailModel.Ev_End_Dt;
                ed = ed.split("T")[0];

                date.setText(sd + " - " + ed);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                date.setText(dataModel.detailModel.Ev_Strt_Dt + " - " + dataModel.detailModel.Ev_End_Dt);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }


    public void setupData(){
        EventDetailMainModel dataModel = global.getEventDetailMainModel();
        String menuTag = getIntent().getStringExtra(GlobalConstants.SELECTED_MENU);

        ((TextView)actionBar.findViewById(R.id.title)).setText(""+menuTag);

        ArrayList<AttendeeDetailCommonModel> detailList = new ArrayList<AttendeeDetailCommonModel>();
        try {
            for (AttendeeDetailCommonModel dtModel : dataModel.attendeeMenuFieldsList) {
                if (dtModel.Category.equals(menuTag)) {
                    if (dtModel.IsEnabled) {
                        detailList.add(dtModel);
                    }
                }
            }
        }
        catch (Exception e){
            Log.e("menu detail",""+e.getStackTrace());
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (AttendeeDetailCommonModel model : detailList) {
            LinearLayout detailItem = (LinearLayout)inflater.inflate(R.layout.menu_detail_item, null);

            String label = model.Label;
            String value = model.Value;

            if(label == null){
                label = "";
            }else if(label.equals("null")){
                label = "";
            }

            if(value == null){
                value = "";
            }else if(value.equals("null")){
                value = "";
            }

            ((TextView)detailItem.findViewById(R.id.label)).setText(""+label);
            ((TextView)detailItem.findViewById(R.id.value)).setText(""+value);

            detailContainer.addView(detailItem);
        }

        detailContainer.invalidate();

        if(detailList.size() == 0)
        {
            Toast.makeText(MenuDetails.this, "No detail found...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceivers();
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

                DatabaseHandler dbHandler = new DatabaseHandler(MenuDetails.this);
                BeaconNotification notification = new BeaconNotification();
                notification.setType(GlobalConstants.PUSHWOOSH_TYPE_PUSH_NOTIFICATION);
                notification.setTitle(pushTitle);
                dbHandler.addNotification(notification);

                AlertDialog.Builder builder = new AlertDialog.Builder(MenuDetails.this);
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
                Log.e("exceptions : ",""+e1.getStackTrace());
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


}