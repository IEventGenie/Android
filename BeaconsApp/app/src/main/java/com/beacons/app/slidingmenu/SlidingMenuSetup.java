package com.beacons.app.slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.beaconsapp.BeaconsListActivity;
import com.beacons.app.beaconsapp.Globals;
import com.beacons.app.beaconsapp.HomeActivity;
import com.beacons.app.beaconsapp.MenuDetails;
import com.beacons.app.beaconsapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by aman on 2/12/16.
 */
public class SlidingMenuSetup {

    Activity attachToAct;
    SlidingMenu menu;
    Globals global;

    public SlidingMenuSetup(Activity act) {
        attachToAct = act;
        global = (Globals) act.getApplicationContext();
    }

    public SlidingMenu setSlidingMenu()
    {
        menu = new SlidingMenu(attachToAct);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow_menu);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(attachToAct, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu);

        setUpData();

        return menu;
    }

    public void setUpData(){
        EventDetailMainModel dataModel = global.getEventDetailMainModel();

        TextView name = (TextView)menu.findViewById(R.id.user_name);
        name.setText(""+dataModel.attendeeDetail.FirstName+" "+dataModel.attendeeDetail.LastName);

        setUpMenuItems(dataModel);
    }

    public void setUpMenuItems(EventDetailMainModel dataModel){

        LayoutInflater inflater = (LayoutInflater) attachToAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout itemsContainer = (LinearLayout)menu.findViewById(R.id.item_container);

        if(dataModel.attendeeDetail.CustomField1.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField1.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField2.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField2.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField3.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField3.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField4.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField4.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField5.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField5.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField6.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField6.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField7.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField7.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField8.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField8.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField9.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField9.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField10.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField10.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField11.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField11.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField12.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField12.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField13.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField13.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField14.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField14.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField15.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField15.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField16.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField16.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField17.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField17.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField18.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField18.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField19.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField19.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }
        if(dataModel.attendeeDetail.CustomField20.IsEnabled){
            LinearLayout lay = (LinearLayout)inflater.inflate(R.layout.menu_item, null);
            ((TextView)lay.findViewById(R.id.title)).setText(dataModel.attendeeDetail.CustomField20.Label);
            lay.setOnClickListener(MenuItemClick);
            itemsContainer.addView(lay);
        }

        itemsContainer.invalidate();
    }

    public View.OnClickListener MenuItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           //attachToAct.startActivity(new Intent(attachToAct, MenuDetails.class));
            attachToAct.startActivity(new Intent(attachToAct, BeaconsListActivity.class));
            menu.showContent();
        }
    };
}