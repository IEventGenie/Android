package com.beacons.app.beaconsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.slidingmenu.SlidingMenuSetup;
import com.beacons.app.utilities.CircleTransform;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;

import java.io.LineNumberReader;


public class MyEventsActivity extends BaseActivity {


    RelativeLayout actionBar;
    ListView eventsList;
    EventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events);

        configActionBar();

        findViewsApplyActions();
    }


    public void configActionBar() {
        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setVisibility(View.GONE);
        ((TextView)actionBar.findViewById(R.id.title)).setText(getResources().getString(R.string.my_events_title));
    }

    public void findViewsApplyActions() {

        eventsList = (ListView) findViewById(R.id.my_event_list);
        adapter = new EventsAdapter(this);
        eventsList.setAdapter(adapter);

        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MyEventsActivity.this,HomeActivity.class));
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