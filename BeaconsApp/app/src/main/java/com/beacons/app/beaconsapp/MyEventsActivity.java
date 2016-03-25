package com.beacons.app.beaconsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.utilities.CircleTransform;
import com.beacons.app.webservices.WebServiceHandler;
import com.squareup.picasso.Picasso;


public class MyEventsActivity extends BaseActivity {


    RelativeLayout actionBar;
    ListView activeEventsList,pastEventsList;
    EventsAdapter adapter;
    RelativeLayout activeTab,pastTab;
    TextView activeText,pastText;
    View activeTabStrip,pastTabStrip;
    final int ActiveTabConst = 1;
    final int PastTabConst = 2;
    int CurrentTab = ActiveTabConst;
    Globals global;
    AlertDialog preChkDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events);

        configActionBar();

        global = (Globals) getApplicationContext();

        findViewsApplyActions();
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

        activeEventsList = (ListView) findViewById(R.id.active_event_list);
        pastEventsList = (ListView) findViewById(R.id.past_event_list);

        adapter = new EventsAdapter(this);
        activeEventsList.setAdapter(adapter);

        activeEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MyEventsActivity.this, HomeActivity.class));
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

    }

    public class EventsAdapter extends BaseAdapter
    {
        LayoutInflater inflater;
        int darkBack,lightBack;
        Globals global;
        EventDetailMainModel dataModel;

        public EventsAdapter(Context c) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            darkBack = getResources().getColor(R.color.my_event_list_color_dark);
            lightBack = getResources().getColor(R.color.my_event_list_color_light);

            global = (Globals) c.getApplicationContext();
            dataModel = global.getEventDetailMainModel();
        }

        @Override
        public int getCount() {
            return 1;
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

            holder.title.setText(""+dataModel.detailModel.Ev_Nm);
            holder.location.setText("" + dataModel.detailModel.Ev_Addr_1_Txt + "," + dataModel.detailModel.Ev_City_Txt);
            Picasso.with(MyEventsActivity.this)
                    .load(""+dataModel.detailModel.Ev_Img_Url)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.icon)
                    .into(holder.eventImg);
            try {
                String date = "" + dataModel.detailModel.Ev_Chk_In_Strt_Dttm;
                date = date.split("T")[0];
                holder.date.setText(date);
            }catch (Exception e){
                System.out.println(e.getStackTrace());
                holder.date.setText("" + dataModel.detailModel.Ev_Chk_In_Strt_Dttm);
            }

            holder.prechkBtn.setTag(position);
            holder.prechkBtn.setOnClickListener(PreCheckClick);

            return convertView;
        }

    }

    public View.OnClickListener PreCheckClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPreCheckinDialog();
        }
    };

    public void showPreCheckinDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to Pre Checkin for the event?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preChkDialog.dismiss();
                EventDetailMainModel mainModel = global.getEventDetailMainModel();
                new ConfirmationCodeService(mainModel.detailModel.Ev_Id, mainModel.attendeeDetail.Id).execute("");
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
        ImageView eventImg;
        TextView title,location,date,prechkBtn;
    }

    public class ConfirmationCodeService extends AsyncTask<String,Integer,GlobalConstants.ResponseStatus> {

        ProgressDialog pd;
        String EventId = "",AttendeeId = "";

        public ConfirmationCodeService(String EventId,String AttendeeId) {
            this.EventId = EventId;
            this.AttendeeId = AttendeeId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(MyEventsActivity.this,"","Checking in...");
        }

        @Override
        protected GlobalConstants.ResponseStatus doInBackground(String... params) {
            GlobalConstants.ResponseStatus res = GlobalConstants.ResponseStatus.Fail;
            try {
                res = WebServiceHandler.submitPreCheckinEvent(MyEventsActivity.this,EventId,AttendeeId);
            }catch (Exception e){
                Log.e("Exception : ", e.getStackTrace().toString());
            }
            return res;
        }

        @Override
        protected void onPostExecute(GlobalConstants.ResponseStatus status) {
            super.onPostExecute(status);
            pd.dismiss();
            if(status == GlobalConstants.ResponseStatus.OK) {
                Toast.makeText(MyEventsActivity.this, getResources().getString(R.string.succ_pre_chkin), Toast.LENGTH_LONG).show();
            }else if(status == GlobalConstants.ResponseStatus.AuthorisationRequired) {

            }else if(status == GlobalConstants.ResponseStatus.Fail){
                Toast.makeText(MyEventsActivity.this, getResources().getString(R.string.error_pre_chkin), Toast.LENGTH_LONG).show();
            }
        }
    }

}