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
import android.widget.ImageView;
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
import com.beacons.app.utilities.CircleTransform;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;

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
    ImageView eventImg;
    TextView titleTxt,location,date;
    EventDetailMainModel dataModel;

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
        setEventData();
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

    public void setEventData(){
        try {
            eventImg = (ImageView) menu.findViewById(R.id.event_img);
            titleTxt = (TextView) menu.findViewById(R.id.title_sec);
            location = (TextView) menu.findViewById(R.id.location);
            date = (TextView) menu.findViewById(R.id.date);

            titleTxt.setText("" + dataModel.detailModel.Ev_Nm);

            location.setText("" + dataModel.detailModel.Ev_Addr_1_Txt + "," + dataModel.detailModel.Ev_City_Txt);
            Picasso.with(attachToAct)
                    .load("" + dataModel.detailModel.Ev_Img_Url)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.icon)
                    .into(eventImg);
            try {
                String dd = "" + dataModel.detailModel.Ev_Chk_In_Strt_Dttm;
                dd = dd.split("T")[0];
                date.setText(dd);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                date.setText("" + dataModel.detailModel.Ev_Chk_In_Strt_Dttm);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
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

}