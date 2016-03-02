package com.beacons.app.beaconsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.utilities.CircleTransform;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events);

        configActionBar();

        findViewsApplyActions();
    }


    public void configActionBar() {
        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setVisibility(View.INVISIBLE);

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

            return convertView;
        }

    }

    public class ViewHolder
    {
        ImageView eventImg;
        TextView title,location,date;

    }

}