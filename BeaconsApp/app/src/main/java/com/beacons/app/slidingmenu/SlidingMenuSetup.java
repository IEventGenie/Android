package com.beacons.app.slidingmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacons.app.WebserviceDataModels.AttendeeDetailCommonModel;
import com.beacons.app.WebserviceDataModels.CategoryCommonModel;
import com.beacons.app.WebserviceDataModels.CategoryTypeModel;
import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.WebserviceDataModels.ResponseModel;
import com.beacons.app.beaconsapp.Globals;
import com.beacons.app.beaconsapp.MenuDetails;
import com.beacons.app.beaconsapp.R;
import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.notificationDb.DatabaseHandler;
import com.beacons.app.notificationDb.EventDetailDBModel;
import com.beacons.app.utilities.CircleTransform;
import com.beacons.app.utilities.Utilities;
import com.beacons.app.webservices.WebServiceHandler;
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
    TextView titleTxt,location, dateOfEvent,preChkin;
    ImageView ChkinImg;
    EventDetailMainModel dataModel;
    AlertDialog preChkDialog;
    EventDetailDBModel dataDBModel;

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

        preChkin = (TextView)menu.findViewById(R.id.pre_chkin_btn);
        ChkinImg = (ImageView)menu.findViewById(R.id.check_in_img);

        setUpData();
        setEventData();
        return menu;
    }

    public void setUpData(){

        menuList = (ExpandableListView)menu.findViewById(R.id.menu_list);

        dataModel = global.getEventDetailMainModel();
        dataDBModel = global.getEventDetailDBModel();

        TextView name = (TextView)menu.findViewById(R.id.user_name);
        name.setText("" + dataModel.attendeeDetail.FirstName + " " + dataModel.attendeeDetail.LastName);

        if(dataModel.detailModel.Ev_Sts_Cd.equals("Closed")){
            preChkin.setVisibility(View.GONE);
            ChkinImg.setVisibility(View.GONE);
        }else{

            String prechkinstatus = ""+dataModel.attendeeDetail.Status;

            if(prechkinstatus.equalsIgnoreCase("PreCheckin") || prechkinstatus.equalsIgnoreCase("Checkedin")){
                preChkin.setVisibility(View.GONE);
                ChkinImg.setVisibility(View.VISIBLE);
            }else{
                preChkin.setVisibility(View.VISIBLE);
                ChkinImg.setVisibility(View.GONE);
            }
        }

        preChkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreCheckinDialog();
            }
        });

        /*String menu1 = "My Reminders";
        listDataHeader.add(menu1);

        ArrayList<String> menuListTemp = new ArrayList<String>();
        int upto = dataModel.categoryDetail.size();
        for (int i=0;i<upto;i++)
        {
            CategoryTypeModel dtModel = dataModel.categoryDetail.get(i);
            String header = ""+dtModel.categoryDetail.Text;
            menuListTemp.add(header);

            ArrayList<String> childTexts = new ArrayList<String>();
            for (CategoryCommonModel model : dtModel.childrenModels) {
                menuListTemp.add(model.Text);
            }
        }

        for (String title : menuListTemp ) {
            upto = dataModel.attendeeMenuFieldsList.size();
            for(int i=0;i<upto;i++){
                String cat = dataModel.attendeeMenuFieldsList.get(i).Category;
                if(cat.equals(title)){
                    if(dataModel.attendeeMenuFieldsList.get(i).IsEnabled){
                        if(!listDataHeader.contains(""+title)){
                            listDataHeader.add(""+title);
                        }
                    }
                }
            }
        }*/

        //setUpMenuItems(dataModel);


        HashMap<String,String> menuParentChildRelation = new HashMap<String,String>();
        ArrayList<String> menuChilds = new ArrayList<String>();

        int upto = dataModel.categoryDetail.size();
        for (int i=0;i<upto;i++)
        {
            CategoryTypeModel dtModel = dataModel.categoryDetail.get(i);
            String header = ""+dtModel.categoryDetail.Text;
            listDataHeader.add(header);

            ArrayList<String> childTexts = new ArrayList<String>();
            for (CategoryCommonModel model : dtModel.childrenModels) {
                childTexts.add(model.Text);
                menuParentChildRelation.put(model.Text, header);
                menuChilds.add(model.Text);
            }
            listChildData.put(header,childTexts);
        }

        ArrayList<String> listDataHeaderTemp = new ArrayList<String>();
        HashMap<String,ArrayList<String>> listChildDataTemp = new HashMap<String,ArrayList<String>>();

        for(String tag : listDataHeader) {
            for (AttendeeDetailCommonModel dtModel : dataModel.attendeeMenuFieldsList) {
                if (dtModel.Category.equals(tag)) {
                    if (dtModel.IsEnabled) {
                        if(listChildData.get(tag).size() == 0){
                            listDataHeaderTemp.add(tag);
                        }
                    }
                }
            }
        }

        for(String childTag : menuChilds) {
            for (AttendeeDetailCommonModel dtModel : dataModel.attendeeMenuFieldsList) {
                if (dtModel.Category.equals(childTag)) {
                    if (dtModel.IsEnabled) {
                        String parent = menuParentChildRelation.get(childTag);
                        if (listChildDataTemp.containsKey(parent)) {
                            ArrayList<String> childList = listChildDataTemp.get(parent);
                            if(!childList.contains(childTag)) {
                                childList.add(childTag);
                                listChildDataTemp.remove(parent);
                                listChildDataTemp.put(parent, childList);
                            }
                        } else {
                            ArrayList<String> childList = new ArrayList<String>();
                            childList.add(childTag);
                            listChildDataTemp.put(parent, childList);
                        }
                    }
                }
            }
        }

        for(String header : listDataHeaderTemp){
            if(!listChildDataTemp.containsKey(header)){
                listChildDataTemp.put(header,new ArrayList<String>());
            }
        }

        listDataHeader.clear();
        listChildData.clear();
        //Static Menu
        String menu1 = "My Event";
        listDataHeader.add(menu1);
        //========

        for ( String key : listChildDataTemp.keySet() ) {
            listDataHeader.add(key);
        }

        listChildData = listChildDataTemp;
        listChildData.put(menu1, new ArrayList<String>());

        MenuExpandableAdapter adapter = new MenuExpandableAdapter(attachToAct,listDataHeader,listChildData);
        menuList.setAdapter(adapter);

        menuList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (listChildData.get(listDataHeader.get(groupPosition)).size() == 0) {
                    //
                    if(groupPosition == 0){
                        attachToAct.finish();
                    }else{
                        Intent intent = new Intent(attachToAct, MenuDetails.class);
                        intent.putExtra(GlobalConstants.SELECTED_MENU, "" + listDataHeader.get(groupPosition));
                        attachToAct.startActivity(intent);
                        menu.showContent();
                    }
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

        /*MenuListAdapter adapter = new MenuListAdapter(attachToAct,listDataHeader);
        menuList.setAdapter(adapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    Intent intent = new Intent(attachToAct, MenuDetails.class);
                    intent.putExtra(GlobalConstants.SELECTED_MENU, "" + listDataHeader.get(position));
                    attachToAct.startActivity(intent);
                    menu.showContent();
                }
            }
        });*/

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
            dateOfEvent = (TextView) menu.findViewById(R.id.date_of_event);

            titleTxt.setText("" + dataModel.detailModel.Ev_Nm);

            location.setText("" + dataModel.detailModel.Ev_Addr_1_Txt + "," + dataModel.detailModel.Ev_City_Txt);
            Picasso.with(attachToAct)
                    .load("" + dataModel.detailModel.Ev_Img_Url)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.icon)
                    .into(eventImg);
            try {
                String sd = "" + dataModel.detailModel.Ev_Strt_Dt;
                sd = sd.split("T")[0];

                String ed = "" + dataModel.detailModel.Ev_End_Dt;
                ed = ed.split("T")[0];

                dateOfEvent.setText(sd + " - " + ed);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                dateOfEvent.setText(dataModel.detailModel.Ev_Strt_Dt + " - " + dataModel.detailModel.Ev_End_Dt);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public class MenuListAdapter extends BaseAdapter{

        private Context _context;
        private ArrayList<String> _listDataHeader;

        public MenuListAdapter(Context context, ArrayList<String> listDataHeader){
            this._context = context;
            this._listDataHeader = listDataHeader;
        }

        @Override
        public int getCount() {
            return _listDataHeader.size();
        }

        @Override
        public Object getItem(int position) {
            return _listDataHeader.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String headerTitle = (String) _listDataHeader.get(position);
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


    public void showPreCheckinDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(attachToAct);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to Pre-Checkin for the event?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preChkDialog.dismiss();
                if(Utilities.isInternetAvailable(attachToAct)) {
                    new ConfirmationCodeService(dataDBModel.getEvId(), dataDBModel.getAttendeeId()).execute("");
                }else{
                    Toast.makeText(attachToAct, attachToAct.getResources().getString(R.string.internet_unavailable), Toast.LENGTH_LONG).show();
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

    public class ConfirmationCodeService extends AsyncTask<String,Integer,ResponseModel> {

        ProgressDialog pd;
        String EventId = "",AttendeeId = "";

        public ConfirmationCodeService(String EventId,String AttendeeId) {
            this.EventId = EventId;
            this.AttendeeId = AttendeeId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(attachToAct,"","Loading...");
        }

        @Override
        protected ResponseModel doInBackground(String... params) {
            ResponseModel returnModel = new ResponseModel();
            returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
            try {
                returnModel = WebServiceHandler.submitPreCheckinEvent(attachToAct, EventId, AttendeeId);
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
                dataDBModel.setEvPreChkinStatus("true");
                DatabaseHandler dbHandler = new DatabaseHandler(attachToAct);
                dbHandler.addEventDetails(dataDBModel);

                preChkin.setVisibility(View.GONE);
                ChkinImg.setVisibility(View.VISIBLE);

                openSuccessfullPreCheckinDialog();
            }else if(res.responseStatus == GlobalConstants.ResponseStatus.AuthorisationRequired) {

            }else if(res.responseStatus == GlobalConstants.ResponseStatus.Fail){
                Toast.makeText(attachToAct, "" + res.message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openSuccessfullPreCheckinDialog(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(attachToAct);
        alertBuilder.setTitle("Pre-Checkin");
        alertBuilder.setMessage(attachToAct.getResources().getString(R.string.succ_pre_chkin));
        alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preChkDialog.dismiss();
            }
        });

        preChkDialog = alertBuilder.create();
        preChkDialog.show();
    }

}