package com.beacons.app.beaconsapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacons.app.WebserviceDataModels.AttendeeDetailCommonModel;
import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.constants.GlobalConstants;

import java.util.ArrayList;

/**
 * Created by aman on 3/1/16.
 */
public class MenuDetails extends BaseActivity {

    RelativeLayout actionBar;
    Globals global;
    LinearLayout detailContainer;

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

        setupData();
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
            ((TextView)detailItem.findViewById(R.id.label)).setText(""+model.Label);
            ((TextView)detailItem.findViewById(R.id.value)).setText(""+model.Value);

            detailContainer.addView(detailItem);
        }

        detailContainer.invalidate();

        if(detailList.size() == 0)
        {
            Toast.makeText(MenuDetails.this, "No detail found...", Toast.LENGTH_SHORT).show();
        }

    }
}