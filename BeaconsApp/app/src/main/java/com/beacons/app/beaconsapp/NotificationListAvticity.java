package com.beacons.app.beaconsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.notificationDb.BeaconNotification;
import com.beacons.app.notificationDb.DatabaseHandler;
import com.beacons.app.slidingmenu.SlidingMenuSetup;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 4/11/16.
 */
public class NotificationListAvticity extends FragmentActivity {

    RelativeLayout actionBar;
    Globals global;
    ListView notiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);

        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setVisibility(View.INVISIBLE);
        actionBar.findViewById(R.id.notification_icon).setVisibility(View.INVISIBLE);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        global.fragActivityPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        global.fragActivityDestroyed();
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