package com.beacons.app.beaconsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.WebserviceDataModels.ResponseModel;
import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.notificationDb.BeaconNotification;
import com.beacons.app.notificationDb.DatabaseHandler;
import com.beacons.app.notificationDb.EventDetailDBModel;
import com.beacons.app.utilities.CircleTransform;
import com.beacons.app.utilities.Utilities;
import com.beacons.app.webservices.ResponseParser;
import com.beacons.app.webservices.WebServiceHandler;
import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.BaseRegistrationReceiver;
import com.pushwoosh.PushManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MyEventsActivity extends FragmentActivity {


    RelativeLayout actionBar;
    SwipeMenuListView activeEventsList,pastEventsList;
    EventsAdapter activeAdapter,pastAdapter;
    RelativeLayout activeTab,pastTab;
    TextView activeText,pastText;
    View activeTabStrip,pastTabStrip;
    final int ActiveTabConst = 1;
    final int PastTabConst = 2;
    int CurrentTab = ActiveTabConst;
    Globals global;
    AlertDialog preChkDialog,notificationDialog;

    ArrayList<EventDetailDBModel> evList;
    ArrayList<EventDetailDBModel> evPastList;
    DatabaseHandler dbHandler;
    public final int FOR_ACTIVE_LIST = 0;
    public final int FOR_PAST_LIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events);

        configActionBar();

        global = (Globals) getApplicationContext();
        global.setFragActivity(this);

        findViewsApplyActions();


        //Register receivers for push notifications
        registerReceivers();

        //Create and start push manager
        PushManager pushManager = PushManager.getInstance(this);

        //Start push manager, this will count app open for Pushwoosh stats as well
        try {
            pushManager.onStartup(this);
        }
        catch(Exception e)
        {
            //push notifications are not available or AndroidManifest.xml is not configured properly
        }

        //Register for push!
        pushManager.registerForPushNotifications();
        checkMessage(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(global.DoLogout){
            this.finish();
        }else {
            global.fragActivityResumed(this.getClass(), MyEventsActivity.this);
            //Re-register receivers on resume
            registerReceivers();
        }

        setEventsList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        global.fragActivityPaused();
        //Unregister receivers on pause
        unregisterReceivers();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        checkMessage(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        global.fragActivityDestroyed();
    }

    public void configActionBar() {
        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setVisibility(View.INVISIBLE);
        actionBar.findViewById(R.id.notification_icon).setVisibility(View.INVISIBLE);

        ((TextView)actionBar.findViewById(R.id.title)).setText(getResources().getString(R.string.my_events_title));

        actionBar.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyEventsActivity.this.finish();
            }
        });
    }

    public void findViewsApplyActions() {

        activeEventsList = (SwipeMenuListView) findViewById(R.id.active_event_list);
        pastEventsList = (SwipeMenuListView) findViewById(R.id.past_event_list);


        activeEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventDetailDBModel model = evList.get(position);
                if(Utilities.isInternetAvailable(MyEventsActivity.this)){
                    global.setEventDetailDBModel(evList.get(position));
                    new GetEventDetailsService(model.getConfirmCode(),model.getLastName()).execute("");
                }else{
                    EventDetailMainModel resultModel = ResponseParser.parseResponseOfEventDetails(""+model.getEventDetailJson());
                    global.setEventDetailMainModel(resultModel);
                    global.setEventDetailJson("" + model.getEventDetailJson());
                    global.setEventDetailDBModel(evList.get(position));
                    startActivity(new Intent(MyEventsActivity.this, HomeActivity.class));
                }
            }
        });

        pastEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventDetailDBModel model = evPastList.get(position);
                if(Utilities.isInternetAvailable(MyEventsActivity.this)){
                    new GetEventDetailsService(model.getConfirmCode(),model.getLastName()).execute("");
                }else{
                    EventDetailMainModel resultModel = ResponseParser.parseResponseOfEventDetails(""+model.getEventDetailJson());
                    global.setEventDetailMainModel(resultModel);
                    global.setEventDetailJson("" + model.getEventDetailJson());
                    startActivity(new Intent(MyEventsActivity.this, HomeActivity.class));
                }
            }
        });

        activeTab = (RelativeLayout) findViewById(R.id.active_event_btn);
        pastTab = (RelativeLayout) findViewById(R.id.past_event_btn);

        activeText = (TextView) findViewById(R.id.active_event_txt);
        pastText = (TextView) findViewById(R.id.past_event_txt);

        activeTabStrip = (View) findViewById(R.id.active_ev_tab_strip);
        pastTabStrip = (View) findViewById(R.id.past_ev_tab_strip);

        activeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentTab != ActiveTabConst) {
                    activeEventsList.setVisibility(View.VISIBLE);
                    pastEventsList.setVisibility(View.GONE);

                    activeText.setTextColor(getResources().getColor(R.color.tab_selected_text_color));
                    pastText.setTextColor(getResources().getColor(R.color.tab_unselected_text_color));

                    activeTabStrip.setVisibility(View.VISIBLE);
                    pastTabStrip.setVisibility(View.GONE);

                    CurrentTab = ActiveTabConst;
                }
            }
        });

        pastTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentTab != PastTabConst) {
                    pastEventsList.setVisibility(View.VISIBLE);
                    activeEventsList.setVisibility(View.GONE);

                    pastText.setTextColor(getResources().getColor(R.color.tab_selected_text_color));
                    activeText.setTextColor(getResources().getColor(R.color.tab_unselected_text_color));

                    pastTabStrip.setVisibility(View.VISIBLE);
                    activeTabStrip.setVisibility(View.GONE);

                    CurrentTab = PastTabConst;
                }
            }
        });

        findViewById(R.id.activate_event_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyEventsActivity.this.finish();
            }
        });

        setListSlidingOptions();

    }

    public void setListSlidingOptions() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Utilities.dp2px(MyEventsActivity.this, 90));
                // set a icon
                //deleteItem.setIcon(R.drawable.warning);
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(14);
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        activeEventsList.setMenuCreator(creator);
        pastEventsList.setMenuCreator(creator);

        // Left
        activeEventsList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        pastEventsList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        activeEventsList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        String id = evList.get(position).getEvId();
                        int res = dbHandler.deleteEvent(id);
                        //if(res == 1){
                            evList.remove(position);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activeAdapter.notifyDataSetChanged();
                                }
                            });
                        //}
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        pastEventsList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        String id = evPastList.get(position).getEvId();
                        int res = dbHandler.deleteEvent(id);
                        //if(res == 1){
                        evPastList.remove(position);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pastAdapter.notifyDataSetChanged();
                            }
                        });
                        //}
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
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

                DatabaseHandler dbHandler = new DatabaseHandler(MyEventsActivity.this);
                BeaconNotification notification = new BeaconNotification();
                notification.setType(GlobalConstants.PUSHWOOSH_TYPE_PUSH_NOTIFICATION);
                notification.setTitle(pushTitle);
                dbHandler.addNotification(notification);

                AlertDialog.Builder builder = new AlertDialog.Builder(MyEventsActivity.this);
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

    public void setEventsList(){
        dbHandler = new DatabaseHandler(MyEventsActivity.this);

        evList = dbHandler.getActiveEvents();
        activeAdapter = new EventsAdapter(MyEventsActivity.this,evList,FOR_ACTIVE_LIST);
        activeEventsList.setAdapter(activeAdapter);

        evPastList = dbHandler.getPastEvents();
        pastAdapter = new EventsAdapter(MyEventsActivity.this,evPastList,FOR_PAST_LIST);
        pastEventsList.setAdapter(pastAdapter);
    }

    public class EventsAdapter extends BaseAdapter
    {
        LayoutInflater inflater;
        int darkBack,lightBack;
        Globals global;
        ArrayList<EventDetailDBModel> evList;
        int which = FOR_ACTIVE_LIST;
        String currentdate;

        public EventsAdapter(Context c,ArrayList<EventDetailDBModel> evList,int which) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            darkBack = getResources().getColor(R.color.my_event_list_color_dark);
            lightBack = getResources().getColor(R.color.my_event_list_color_light);

            global = (Globals) c.getApplicationContext();
            this.evList = evList;
            this.which = which;
            currentdate = Utilities.getDateTime();
        }

        @Override
        public int getCount() {
            return evList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView == null){
                convertView = (RelativeLayout)inflater.inflate(R.layout.my_events_list_item,null);

                holder = new ViewHolder();

                holder.eventImg = (ImageView) convertView.findViewById(R.id.event_img);
                holder.tick_img = (ImageView) convertView.findViewById(R.id.tick_img);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.location = (TextView) convertView.findViewById(R.id.location);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.prechkBtn = (TextView) convertView.findViewById(R.id.pre_chk_btn);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            if((position % 2) == 0) {
                convertView.setBackgroundColor(darkBack);
            }else{
                convertView.setBackgroundColor(lightBack);
            }

            EventDetailDBModel dataModel = evList.get(position);

            holder.title.setText(""+dataModel.getEvName());
            holder.location.setText("" + dataModel.getEvAddrTxt() + "," + dataModel.getEvCityTxt());
            try {
                Picasso.with(MyEventsActivity.this)
                        .load(""+dataModel.getEvImgUrl())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.icon)
                        .into(holder.eventImg);
            }
            catch (Exception e){
                Log.e("picasso",""+e.getStackTrace());
            }

            try {
                String date = "" + dataModel.getEvStartDate();
                date = date.split("T")[0];
                holder.date.setText(date);

                String end_date = "" + dataModel.getEvEndDate();
                end_date = end_date.split("T")[0];
                holder.date.setText(date+" - "+end_date);

            }catch (Exception e){
                System.out.println(e.getStackTrace());
                holder.date.setText("" + dataModel.getEvStartDate()+" - "+dataModel.getEvEndDate());
            }

            if(which == FOR_PAST_LIST){
                holder.prechkBtn.setVisibility(View.GONE);
                holder.tick_img.setVisibility(View.GONE);
            }else{
                try {
                    if(Boolean.parseBoolean(dataModel.getEnablePrechkIn())) {
                        //Check pre checkin allowed date
                        try {
                            String startdate = dataModel.getPreChkInStrtDate().replace("T", " ");
                            String enddate = dataModel.getPreChkInEndDate().replace("T", " ");
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date start = formatter.parse(startdate);
                            Date end = formatter.parse(enddate);
                            Date current = formatter.parse(currentdate);

                            if (current.after(start) && current.before(end)) {
                                if(dataModel.getEvPreChkinStatus().equals(GlobalConstants.STATUS_PRE_CHK_IN) ||
                                        dataModel.getEvPreChkinStatus().equals(GlobalConstants.STATUS_CHECKED_IN)){
                                    holder.prechkBtn.setVisibility(View.GONE);
                                    holder.tick_img.setVisibility(View.VISIBLE);
                                }else{
                                    holder.prechkBtn.setVisibility(View.VISIBLE);
                                    holder.tick_img.setVisibility(View.GONE);
                                }
                            } else {
                                holder.prechkBtn.setVisibility(View.GONE);
                                holder.tick_img.setVisibility(View.VISIBLE);
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }else {
                        if(dataModel.getEvPreChkinStatus().equals(GlobalConstants.STATUS_PRE_CHK_IN) ||
                                dataModel.getEvPreChkinStatus().equals(GlobalConstants.STATUS_CHECKED_IN)){
                            holder.prechkBtn.setVisibility(View.GONE);
                            holder.tick_img.setVisibility(View.VISIBLE);
                        }else{
                            holder.prechkBtn.setVisibility(View.GONE);
                            holder.tick_img.setVisibility(View.GONE);
                        }
                    }
                }
                catch (Exception e1) {
                    holder.prechkBtn.setVisibility(View.GONE);
                    holder.tick_img.setVisibility(View.GONE);
                    Log.e("exception","e1 "+e1.getStackTrace());
                }
            }

            holder.prechkBtn.setTag(position);
            holder.prechkBtn.setOnClickListener(PreCheckClick);

            return convertView;
        }
    }

    public View.OnClickListener PreCheckClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int pos = Integer.parseInt(v.getTag().toString());
            showPreCheckinDialog(pos);
        }
    };

    public void showPreCheckinDialog(final int pos)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to Pre-Checkin for the event?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            preChkDialog.dismiss();
            if(Utilities.isInternetAvailable(MyEventsActivity.this)) {
                EventDetailDBModel dbModel = evList.get(pos);
                new ConfirmationCodeService(dbModel.getEvId(),dbModel.getAttendeeId(),pos).execute("");
            }else{
                Toast.makeText(MyEventsActivity.this, getResources().getString(R.string.internet_unavailable),Toast.LENGTH_LONG).show();
            }
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preChkDialog.dismiss();
            }
        });

        preChkDialog = builder.create();
        preChkDialog.show();
    }

    public class ViewHolder
    {
        ImageView eventImg,tick_img;
        TextView title,location,date,prechkBtn;
    }

    public class ConfirmationCodeService extends AsyncTask<String,Integer,ResponseModel> {

        ProgressDialog pd;
        String EventId = "",AttendeeId = "";
        int pos;

        public ConfirmationCodeService(String EventId,String AttendeeId,int pos) {
            this.EventId = EventId;
            this.AttendeeId = AttendeeId;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(MyEventsActivity.this,"","Loading...");
        }

        @Override
        protected ResponseModel doInBackground(String... params) {
            ResponseModel returnModel = new ResponseModel();
            returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
            try {
                returnModel = WebServiceHandler.submitPreCheckinEvent(MyEventsActivity.this,EventId,AttendeeId);
            }catch (Exception e){
                Log.e("Exception : ", e.getStackTrace().toString());
            }
            return returnModel;
        }

        @Override
        protected void onPostExecute(ResponseModel res) {
            super.onPostExecute(res);
            pd.dismiss();
            if(res.responseStatus == GlobalConstants.ResponseStatus.OK) {
                //Toast.makeText(MyEventsActivity.this, getResources().getString(R.string.succ_pre_chkin), Toast.LENGTH_LONG).show();
                EventDetailDBModel dbModel = evList.get(pos);
                dbModel.setEvPreChkinStatus(GlobalConstants.STATUS_PRE_CHK_IN);
                DatabaseHandler dbHandler = new DatabaseHandler(MyEventsActivity.this);
                dbHandler.addEventDetails(dbModel);

                evList = dbHandler.getActiveEvents();
                activeAdapter.notifyDataSetChanged();

                openSuccessfullPreCheckinDialog();
            }else if(res.responseStatus == GlobalConstants.ResponseStatus.AuthorisationRequired) {

            }else if(res.responseStatus == GlobalConstants.ResponseStatus.Fail){
                Toast.makeText(MyEventsActivity.this, ""+res.message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openSuccessfullPreCheckinDialog(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MyEventsActivity.this);
        alertBuilder.setTitle("Pre-Checkin");
        alertBuilder.setMessage(getResources().getString(R.string.succ_pre_chkin));
        alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preChkDialog.dismiss();
            }
        });

        preChkDialog = alertBuilder.create();
        preChkDialog.show();
    }

    public class GetEventDetailsService extends AsyncTask<String,Integer,GlobalConstants.ResponseStatus>{

        ProgressDialog pd;
        String confirmationCode = "",lastName = "";

        public GetEventDetailsService(String confirmationCode,String lastN) {
            this.confirmationCode = confirmationCode;
            this.lastName = lastN;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(MyEventsActivity.this,"","Loading...");
        }

        @Override
        protected GlobalConstants.ResponseStatus doInBackground(String... params) {
            GlobalConstants.ResponseStatus res = GlobalConstants.ResponseStatus.Fail;
            try {
                res = WebServiceHandler.getEventsListByAccessCode(MyEventsActivity.this, confirmationCode, lastName);
            }catch (Exception e){
                Log.e("Exception : ",e.getStackTrace().toString());
            }
            return res;
        }

        @Override
        protected void onPostExecute(GlobalConstants.ResponseStatus status) {
            super.onPostExecute(status);
            pd.dismiss();
            if(status == GlobalConstants.ResponseStatus.OK) {
                try{
                    DatabaseHandler dbHandler = new DatabaseHandler(MyEventsActivity.this);
                    EventDetailDBModel dbModel = new EventDetailDBModel();
                    EventDetailMainModel dataModel = global.getEventDetailMainModel();

                    dbModel.setConfirmCode(confirmationCode);
                    dbModel.setLastName(lastName);
                    dbModel.setEvId(dataModel.detailModel.Ev_Id);
                    dbModel.setEvAddrTxt(dataModel.detailModel.Ev_Addr_1_Txt);
                    dbModel.setEvCityTxt(dataModel.detailModel.Ev_City_Txt);
                    dbModel.setEvImgUrl(dataModel.detailModel.Ev_Img_Url);
                    dbModel.setEvLocTxt(dataModel.detailModel.Ev_Loc_Txt);
                    dbModel.setEvName(dataModel.detailModel.Ev_Nm);
                    dbModel.setEnablePrechkIn("" + dataModel.detailModel.Enabl_PreCheckin);
                    dbModel.setAttendeeId(dataModel.attendeeDetail.Id);
                    dbModel.setChkinStartDate(dataModel.detailModel.Ev_Chk_In_Strt_Dttm);
                    dbModel.setChkInEndDate(dataModel.detailModel.Ev_Chk_In_End_Dttm);
                    dbModel.setPreChkInStrtDate(dataModel.detailModel.Ev_Early_Chk_In_Strt_Dttm);
                    dbModel.setPreChkInEndDate(dataModel.detailModel.Ev_Early_Chk_In_End_Dttm);
                    dbModel.setEnablCheckin(dataModel.detailModel.Enabl_Checkin);
                    dbModel.setEventStatus(dataModel.detailModel.Ev_Sts_Cd);
                    dbModel.setEvStartDate(dataModel.detailModel.Ev_Strt_Dt);
                    dbModel.setEvEndDate(dataModel.detailModel.Ev_End_Dt);
                    String preChkStatus = ""+dataModel.attendeeDetail.Status;
                    if(preChkStatus.length() == 0 || preChkStatus.equals("null")){
                        preChkStatus = "false";
                    }
                    dbModel.setEvPreChkinStatus(preChkStatus);
                    dbModel.setEventDetailJson(global.getEventDetailJson());

                    dbHandler.addEventDetails(dbModel);
                }catch (Exception e){
                    Log.e("update",""+e.getStackTrace());
                }

                startActivity(new Intent(MyEventsActivity.this, HomeActivity.class));
            }else if(status == GlobalConstants.ResponseStatus.AuthorisationRequired) {

            }else if(status == GlobalConstants.ResponseStatus.Fail){
                Toast.makeText(MyEventsActivity.this, getResources().getString(R.string.error_getting_details),Toast.LENGTH_LONG).show();
            }
        }
    }
}