package com.beacons.app.slidingmenu;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beacons.app.WebserviceDataModels.CategoryCommonModel;
import com.beacons.app.WebserviceDataModels.CategoryTypeModel;
import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.beaconsapp.BeaconsListActivity;
import com.beacons.app.beaconsapp.CodeEntryAvtivity;
import com.beacons.app.beaconsapp.Globals;
import com.beacons.app.beaconsapp.HomeActivity;
import com.beacons.app.beaconsapp.MenuDetails;
import com.beacons.app.beaconsapp.R;
import com.beacons.app.constants.GlobalConstants;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aman on 2/12/16.
 */
public class SlidingMenuSetup {

    Activity attachToAct;
    SlidingMenu menu;
    Globals global;
    ExpandableListView menuList;
    ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, ArrayList<String>> listChildData = new HashMap<String, ArrayList<String>>();

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

        menuList = (ExpandableListView)menu.findViewById(R.id.menu_list);

        EventDetailMainModel dataModel = global.getEventDetailMainModel();

        TextView name = (TextView)menu.findViewById(R.id.user_name);
        name.setText("" + dataModel.attendeeDetail.FirstName + " " + dataModel.attendeeDetail.LastName);

        //setUpMenuItems(dataModel);
        int upto = dataModel.categoryDetail.size();
        for (int i=0;i<upto;i++)
        {
            CategoryTypeModel dtModel = dataModel.categoryDetail.get(i);
            String header = ""+dtModel.categoryDetail.Text;
            listDataHeader.add(header);

            ArrayList<String> childTexts = new ArrayList<String>();
            for (CategoryCommonModel model : dtModel.childrenModels) {
                childTexts.add(model.Text);
            }

            listChildData.put(header,childTexts);
        }

        MenuExpandableAdapter adapter = new MenuExpandableAdapter(attachToAct,listDataHeader,listChildData);
        menuList.setAdapter(adapter);

        menuList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (listChildData.get(listDataHeader.get(groupPosition)).size() == 0) {
                    //
                    Intent intent = new Intent(attachToAct, MenuDetails.class);
                    intent.putExtra(GlobalConstants.SELECTED_MENU, "" + listDataHeader.get(groupPosition));
                    attachToAct.startActivity(intent);
                    menu.showContent();
                }
                return false;
            }
        });

        menuList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(attachToAct, MenuDetails.class);
                intent.putExtra(GlobalConstants.SELECTED_MENU, "" + listChildData.get(listDataHeader.get(groupPosition)).get(childPosition));
                attachToAct.startActivity(intent);
                menu.showContent();
                return false;
            }
        });

        menu.findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.DoLogout = true;
                attachToAct.finish();
                /*android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                Intent intent = new Intent(attachToAct, CodeEntryAvtivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                attachToAct.startActivity(intent);*/
            }
        });
    }

    public class MenuExpandableAdapter extends BaseExpandableListAdapter{

        private Context _context;
        private ArrayList<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, ArrayList<String>> _listDataChild;

        public MenuExpandableAdapter(Context context, ArrayList<String> listDataHeader,
                                     HashMap<String, ArrayList<String>> listChildData){
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.menu_list_header, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            //lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.menu_list_child, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    /*public void setUpMenuItems(EventDetailMainModel dataModel){

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
    };*/
}